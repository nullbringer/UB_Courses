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




def convolve_img(img, kernel,kernel_radius):


	height, width = img.shape
	output_image = [[0 for col in range(width)] for row in range(height)]
	

	# ignoring edge pixels for now.
	# add padding zero

	for i in range(kernel_radius, height-kernel_radius):
		for j in range(kernel_radius, width-kernel_radius):

			# elementwise multiplication sum
			
			loop_end = (kernel_radius*2)+1

			sum = 0
			for x in range(0,loop_end):
				for y in range(0,loop_end):
					sum += kernel[x][y] * img[i-kernel_radius+x][j-kernel_radius+y]

			output_image[i][j] = sum


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
                    [ 0, 1, 2], 
                    [ -1, 0, 1], 
                    [ -2, -1 , 0] 
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
    print(max_val)

    for i in range(0,h):
        for j in range(1,w):
            k = (edge_x_img[i][j]/max_val)*255
            if k>20:
                pos_edge_x[i][j] = 255
            else:
                pos_edge_x[i][j] = 0
            


    
    pos_edge_x = np.asarray(pos_edge_x)
    return pos_edge_x



def cast_vote(accumulator, x, y):

    # value of theta needs to be cycled from -90 to +90 degrees and get the correspondent value of p
    # p = x cosθ + y sinθ

    rw,cl = accumulator.shape 

    for theta in range(-180, 180):

        theta_rad = math.radians(theta)

        p = ((x * math.cos(theta_rad)) +  (y * math.sin(theta_rad)))
        # print(p)

        # print(accumulator)
        if p<cl and p>-1:
            accumulator[theta+180][int(p)] +=1



    return accumulator


def mark_lines(max_theta, max_p, img):
    
    theta_rad = math.radians(max_theta)
    
    h, w, _ = img.shape
    
    for i in range(h):
        # p = x cosθ + y sinθ
        # x = (p - y sinθ) / cosθ
        j = int((max_p - (i * math.sin(theta_rad))) / math.cos(theta_rad))
        if j<w and j> -1:
            img[i][j] = [0,255,0]
    return img









def main():

	#task 1

	hough_img = cv2.imread("original_imgs/hough.jpg", 0)


	simg = convolve_img(hough_img, get_gaussian_kernel(math.sqrt(2)),3)
	xedge = edge_detection_x(simg)


	h, w = hough_img.shape

	diagonal_length = math.ceil(math.sqrt(h**2 + w**2))

	accumulator = np.zeros([360,diagonal_length*2])

	for i in range(h):
		for j in range(w):
			if xedge[i][j] >100:
				accumulator = cast_vote(accumulator, y=i, x=j)



	write_image(accumulator,'output/accumulator')


	hough_img = cv2.imread("original_imgs/hough.jpg")
	hough_img_red = hough_img.copy()


	co_p_t = np.unravel_index(np.argsort(accumulator.ravel())[-1500:], accumulator.shape)

	for z in range(len(co_p_t[0])):
		max_theta = co_p_t[0][z]-180
		max_p = co_p_t[1][z]

		angle_theshold = 2
		if abs(max_theta) <angle_theshold:
			hough_img_red = mark_lines(max_theta, max_p, hough_img_red)


	hough_img_blue = hough_img.copy()
	co_p_t = np.unravel_index(np.argsort(accumulator.ravel())[-3500:], accumulator.shape)

	for z in range(len(co_p_t[0])):
		max_theta = co_p_t[0][z]-180
		max_p = co_p_t[1][z]

		angle_theshold = 2
		if abs(max_theta-145) <angle_theshold or abs(max_theta+38) <angle_theshold:
			hough_img_blue = mark_lines(max_theta, max_p, hough_img_blue)
	    




	write_image(hough_img_blue,'output/blue_line')
	write_image(hough_img_red,'output/red_line')





main()