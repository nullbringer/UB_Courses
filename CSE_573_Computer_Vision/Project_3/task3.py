import cv2
import numpy as np
import math
from matplotlib import pyplot as plt


def print_image(img, image_name):
	cv2.namedWindow(image_name, cv2.WINDOW_NORMAL)
	cv2.imshow(image_name, img)
	cv2.waitKey(0)
	cv2.destroyAllWindows()
	

def write_image(img, image_name):
	cv2.imwrite(image_name + '.png',img)




def add_padding(image, padding_width, background_value):

	h, w = image.shape

	_padded_img = [[background_value for col in range(w + (padding_width*2))] for row in range(h + (padding_width*2))]

	for i in range(h):
		for j in range(w):
			_padded_img[i + padding_width][j + padding_width] = image[i][j]


	return np.asarray(_padded_img)



def remove_padding(image, padding_width):

	h, w = image.shape

	_no_padded_img = [[0 for col in range(w - (padding_width*2))] for row in range(h - (padding_width*2))]

	for i in range(padding_width, h-padding_width):
		for j in range(padding_width, w-padding_width):
			_no_padded_img[i - padding_width][j - padding_width] = image[i][j]


	return np.asarray(_no_padded_img)





def convolve_img(img, kernel,kernel_radius,  background_value=0.):

    padded_img = add_padding(image = img, padding_width = kernel_radius, background_value = background_value)

    height, width = img.shape
    output_image = [[0 for col in range(width)] for row in range(height)]


    for i in range(kernel_radius, height-kernel_radius):
        for j in range(kernel_radius, width-kernel_radius):

            # elementwise multiplication sum

            loop_end = (kernel_radius*2)+1

            sum = 0
            for x in range(0,loop_end):
                for y in range(0,loop_end):
                    sum += kernel[x][y] * padded_img[i-kernel_radius+x][j-kernel_radius+y]

            output_image[i][j] = sum


    remove_padding(image = np.asarray(output_image), padding_width = kernel_radius)
    return np.asarray(output_image)




def gaussian(x, mu, sigma):
    return math.exp( -(((x-mu)/(sigma))**2)/2.0 )


def get_gaussian_kernel(sigma):

    kernel_radius = 3

    # compute the actual kernel elements
    hkernel = [gaussian(x, kernel_radius, sigma) for x in range(2*kernel_radius+1)]
    vkernel = [x for x in hkernel]
    kernel2d = [[xh*xv for xh in hkernel] for xv in vkernel]


    # normalize the kernel elements
    kernelsum = sum([sum(row) for row in kernel2d])
    kernel2d = [[x/kernelsum for x in row] for row in kernel2d]

    return kernel2d


def edge_detection_x(img):

    x_kernel = [				
                    [ -1, 0, 1], 
                    [ -2, 0, 2], 
                    [ -1, 0 , 1] 
                ]  

    edge_x_img = convolve_img(img,x_kernel,1)

    # print_image(edge_x_img,'x edge')
    h,w = edge_x_img.shape

    max_val = 0
    for i in range(0,h):
        for j in range(1,w):
            edge_x_img[i][j] = abs(edge_x_img[i][j])
            max_val = max(max_val,edge_x_img[i][j])

    pos_edge_x = [[0.0 for col in range(w)] for row in range(h)]
    # print(max_val)

    for i in range(0,h):
        for j in range(1,w):
            k = (edge_x_img[i][j]/max_val)*255
            if k>20:
                pos_edge_x[i][j] = 255
            else:
                pos_edge_x[i][j] = 0
            


    
    pos_edge_x = np.asarray(pos_edge_x)
    return pos_edge_x

def edge_detection_y(img, threshold):

    y_kernel = [
                [-1, -2, -1], 
                [0, 0, 0], 
                [1, 2 , 1] 
                ]  

    edge_y_img = convolve_img(img,y_kernel,1, background_value = 220)

    # print_image(edge_x_img,'x edge')
    h,w = edge_y_img.shape

    max_val = 0
    for i in range(0,h):
        for j in range(1,w):
            edge_y_img[i][j] = abs(edge_y_img[i][j])
            max_val = max(max_val,edge_y_img[i][j])

    pos_edge_y = [[0.0 for col in range(w)] for row in range(h)]
    print(max_val)

    for i in range(0,h):
        for j in range(1,w):
            k = (edge_y_img[i][j]/max_val)*255
            if k>threshold:
                pos_edge_y[i][j] = 255
            else:
                pos_edge_y[i][j] = 0
            


    
    pos_edge_y = np.asarray(pos_edge_y)
    return pos_edge_y


def cast_vote(accumulator, x, y, p_offset):

    # value of theta needs to be cycled from -90 to +90 degrees and get the correspondent value of p
    # p = x cosθ + y sinθ

    rw,cl = accumulator.shape 

    for theta in range(-90, 90):

        theta_rad = math.radians(theta)

        p = int(round((x * math.cos(theta_rad)) +  (y * math.sin(theta_rad))))
        # print(p)

        accumulator[theta+90][p+p_offset] +=1



    return accumulator


def mark_lines(max_theta, max_p, img):
    
    theta_rad = math.radians(max_theta)
    
    h, w, _ = img.shape
    
    for i in range(h):
        # p = x cosθ + y sinθ
        # x = (p - y sinθ) / cosθ
        j = int(round((max_p - (i * math.sin(theta_rad))) / math.cos(theta_rad)))
        if j<w and j> -1:
            img[i][j] = [0,255,0]
    return img


def cast_vote_circles(accumulator, x, y, radius):

    rw,cl = accumulator.shape 
    
#     for radius in range(17,23):

    for theta in range(360):

        theta_rad = math.radians(theta)

        a = int(round(x - (radius*math.cos(theta_rad))))

        b = int(round(y + (radius*math.sin(theta_rad))))

        if (a<cl and a>-1) and (b<rw and b>-1):
            accumulator[b][a] +=1



    return accumulator


def mark_circles(a, b, img, radius):
    
    h, w, _ = img.shape
            
            
#     for radius in range(17,23):
    for angle in range(0, 360):
        x = int(round(radius * math.sin(math.radians(angle)) + a))
        y = int(round(radius * math.cos(math.radians(angle)) + b))

        if (x<w and x> -1) and (y<h and y> -1):
            img[y][x] = [0,255,255]           

    return img



def main():

	#task 3 (a)

	hough_img = cv2.imread("original_imgs/hough.jpg", 0)


	simg = convolve_img(hough_img, get_gaussian_kernel(math.sqrt(2)),3)
	xedge = edge_detection_x(simg)


	h, w = hough_img.shape

	diagonal_length = math.ceil(math.sqrt(h**2 + w**2))

	accumulator = np.zeros([180,diagonal_length*2])

	for i in range(h):
		for j in range(w):
			if xedge[i][j] >100:
				accumulator = cast_vote(accumulator, y=i, x=j, p_offset = diagonal_length)



	write_image(accumulator,'output/accumulator_line')


	hough_img = cv2.imread("original_imgs/hough.jpg")
	hough_img_red = hough_img.copy()


	co_p_t = np.unravel_index(np.argsort(accumulator.ravel())[-1500:], accumulator.shape)

	for z in range(len(co_p_t[0])):
		max_theta = co_p_t[0][z]-90
		max_p = co_p_t[1][z]-diagonal_length

		angle_theshold = 1
		if abs(max_theta+2) <angle_theshold:
			hough_img_red = mark_lines(max_theta, max_p, hough_img_red)


	
	write_image(hough_img_red,'output/red_line')


	# task 3(b)

	hough_img_blue = hough_img.copy()
	co_p_t = np.unravel_index(np.argsort(accumulator.ravel())[-2800:], accumulator.shape)

	for z in range(len(co_p_t[0])):
		max_theta = co_p_t[0][z]-90
		max_p = co_p_t[1][z]-diagonal_length

		angle_theshold = 1
		if abs(max_theta+36) <angle_theshold:
			hough_img_blue = mark_lines(max_theta, max_p, hough_img_blue)


	write_image(hough_img_blue,'output/blue_line')


	#task 3(c)
	hough_img = cv2.imread("original_imgs/hough.jpg",0)
	simg = hough_img.copy()
	simg = convolve_img(hough_img, get_gaussian_kernel(math.sqrt(2)),3)

	xedge = edge_detection_y(simg, threshold =15)

	write_image(xedge,'output/filtered')

	r = 20

	h, w = hough_img.shape


	accumulator = np.zeros(hough_img.shape)

	for i in range(h):
	    for j in range(w):
	        if xedge[i][j] !=0:
	            
	            accumulator = cast_vote_circles(accumulator, y=i, x=j, radius = r)


	write_image(accumulator,'output/accumulator')

	hough_img_col = cv2.imread("original_imgs/hough.jpg")
	cirles = hough_img_col.copy()


	co_p_t = np.unravel_index(np.argsort(accumulator.ravel())[-600:], accumulator.shape)
	# print(co_p_t)

	for z in range(len(co_p_t[0])):
	    b = co_p_t[0][z]
	    a = co_p_t[1][z]
	    
	    cirles = mark_circles(a, b, cirles, r)


	write_image(cirles,'output/coin')






main()

