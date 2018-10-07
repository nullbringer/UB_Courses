import cv2
import numpy as np
from math import sqrt
from math import exp



def print_image(img, image_name):
	cv2.namedWindow(image_name, cv2.WINDOW_NORMAL)
	cv2.imshow(image_name, img)
	cv2.waitKey(0)
	cv2.destroyAllWindows()
	

def write_image(img, image_name):
	cv2.imwrite(image_name + '.png',img)





def convolve_img(img, kernel,kernel_radius):


	height, width = img.shape

	output_image = np.zeros(img.shape, np.int)

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


	return output_image



def edge_detection_x(img):

	x_kernel = np.array([				
							[-1, 0, 1], 
							[-2, 0, 2], 
							[-1, 0 , 1] 
						], np.float32)  

	edge_x_img = convolve_img(img,x_kernel,1)

	# print_image(edge_x_img,'x edge')
	h,w = edge_x_img.shape

	max_val = 0
	for i in range(0,h):
		for j in range(1,w):
			edge_x_img[i][j] = abs(edge_x_img[i][j])
			max_val = max(max_val,edge_x_img[i][j])

	pos_edge_x = np.zeros(img.shape, np.float32)

	for i in range(0,h):
		for j in range(1,w):
			pos_edge_x[i][j] = edge_x_img[i][j]/max_val

	
	print_image(pos_edge_x,'x_edge_detection_normalized')




def edge_detection_y(img):

	y_kernel = np.array([				
							[-1, -2, -1], 
							[0, 0, 0], 
							[1, 2 , 1] 
						], np.float32)  


	edge_y_img = convolve_img(img,y_kernel,1)
	# print_image(edge_y_img,'edge_y_img')

	h,w = edge_y_img.shape

	max_val = 0
	for i in range(0,h):
		for j in range(1,w):
			edge_y_img[i][j] = abs(edge_y_img[i][j])
			max_val = max(max_val,edge_y_img[i][j])

	pos_edge_y = np.zeros(img.shape, np.float32)

	for i in range(0,h):
		for j in range(1,w):
			pos_edge_y[i][j] = edge_y_img[i][j]/max_val

	
	print_image(pos_edge_y,'y_edge_detectioin_normalized')


def gaussian(x, mu, sigma):
  return exp( -(((x-mu)/(sigma))**2)/2.0 )


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


def resize_image_to_half(img):

	height, width = img.shape

	output_image = np.zeros((int(height/2), int(width/2)), np.int)


	i_op = 0
	for i in range(0,height):
		j_op = 0

		if i%2 == 0:
				continue

		for j in range(0, width):
			if j%2 == 0:
				continue

			output_image[i_op][j_op] = img[i][j] 
			j_op+=1

		i_op+=1

	
	return output_image



def generate_gaussian_blur_for_an_image(img, octav_id, sigma_row):


	for i in range(len(sigma_row)):		

		gussian_blurred_img = convolve_img(img, get_gaussian_kernel(sigma_row[i]), 3)
		
		write_image(gussian_blurred_img,'gb_img_'+ octav_id +'_'+str(i))




def generate_octavs(image_1,sigma_table):
	
	# octav 1: original image 

	write_image(image_1,'octav_1_original')
	generate_gaussian_blur_for_an_image(image_1,'octav_1', sigma_table[0])



	# octav 2: original image/2
	image_2 = resize_image_to_half(image_1)
	write_image(image_2,'octav_2_original')
	generate_gaussian_blur_for_an_image(image_2,'octav_2', sigma_table[1])




	# octav 3: original image/4
	image_3 = resize_image_to_half(image_2)
	write_image(image_3,'octav_3_original')
	generate_gaussian_blur_for_an_image(image_3,'octav_3', sigma_table[2])


	# octav 4: original image/8
	image_4 = resize_image_to_half(image_3)
	write_image(image_4,'octav_4_original')
	generate_gaussian_blur_for_an_image(image_4,'octav_4', sigma_table[3])




def compute_DoG(list):

	

	for j in range(1,5):
		for i in range(0,4):
		

			img_lower_blur = cv2.imread("gb_img_octav_" + str(j) + "_" + str(i) + ".png", 0)
			img_higher_blur = cv2.imread("gb_img_octav_" + str(j) + "_" + str(i+1) + ".png", 0)

		
			


			height, width = img_lower_blur.shape
			difference = np.zeros(img_lower_blur.shape, dtype=int)


			for h in range(0,height):
				for w in range(0, width):
					difference[h][w] = int(img_higher_blur[h][w]) - int(img_lower_blur[h][w])



			
			write_image(difference,'dog_octav_'+ str(j)+'_'+ str(i))
			
			list.append(difference)

	return list



def find_marked_maxima_minima(dog_top, dog_middle, dog_bottom, scale_multiplier, original_img):

	height, width = dog_middle.shape


	# traversing image
	# ignoring edge pixels for now.
	# add padding zero

	for h in range(1,height-1):
		for w in range(1, width-1):

			#threshold
			if dog_middle[h][w]<2:
				continue



			# traversing and comparing 26 neighbours
			is_maxima = True

			for i in range(h-1,h+2):
				for j in range(w-1,w+2):
					if (dog_middle[h][w] < dog_middle[i][j]) or (dog_middle[h][w] < dog_top[i][j]) or (dog_middle[h][w] < dog_bottom[i][j]):
						is_maxima = False
						break

				if not is_maxima:
					break

			if is_maxima:
				original_img[h*scale_multiplier][w*scale_multiplier] = 255
			else:

				is_minima = False

				for i in range(h-1,h+2):
					for j in range(w-1,w+2):
						if (dog_middle[h][w] > dog_middle[i][j]) or (dog_middle[h][w] > dog_top[i][j]) or (dog_middle[h][w] > dog_bottom[i][j]):
							is_minima = False
							break

					if not is_minima:
						break
				if is_minima:
					original_img[h*scale_multiplier][w*scale_multiplier] = 255

	#print_image(original_img,'keypoints'+str(layer)+str(h)+str(w))
	return original_img

def find_keypoints(original_img, list_of_dog):


	# scale_multiplier = 1

	# #traversing 4 octavs
	# for octav_num in range(1,5):

	# 	# traversing 2 middle layers
	# 	for layer in range(1,3):
			
	# 		original_img = find_marked_maxima_minima(list_of_dog[], scale_multiplier, original_img)

	# 	scale_multiplier = scale_multiplier*2


	find_marked_maxima_minima(list_of_dog[0],list_of_dog[1],list_of_dog[2], 1, original_img)
	find_marked_maxima_minima(list_of_dog[1],list_of_dog[2],list_of_dog[3], 1, original_img)

	find_marked_maxima_minima(list_of_dog[4],list_of_dog[5],list_of_dog[6], 2, original_img)
	find_marked_maxima_minima(list_of_dog[5],list_of_dog[6],list_of_dog[7], 2, original_img)

	find_marked_maxima_minima(list_of_dog[8],list_of_dog[9],list_of_dog[10], 4, original_img)
	find_marked_maxima_minima(list_of_dog[9],list_of_dog[10],list_of_dog[11], 4, original_img)

	find_marked_maxima_minima(list_of_dog[12],list_of_dog[13],list_of_dog[14], 8, original_img)
	find_marked_maxima_minima(list_of_dog[13],list_of_dog[14],list_of_dog[15], 8, original_img)




	write_image(original_img,'keypoints')

def match_template(original_image, laplacian_img, template, output_folder):

	w, h = template.shape[::-1]


	# All the 6 methods for comparison in a list
	methods = ['cv2.TM_CCOEFF', 'cv2.TM_CCOEFF_NORMED', 'cv2.TM_CCORR',
	            'cv2.TM_CCORR_NORMED', 'cv2.TM_SQDIFF', 'cv2.TM_SQDIFF_NORMED']

	for meth in methods:

		oi = original_image.copy()
		img = laplacian_img.copy()
		method = eval(meth)

		# Apply template Matching
		res = cv2.matchTemplate(img,template,method)

		min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(res)

		if method in [cv2.TM_SQDIFF, cv2.TM_SQDIFF_NORMED]:
		    top_left = min_loc
		else:
		    top_left = max_loc
		bottom_right = (top_left[0] + w, top_left[1] + h)

		cv2.rectangle(oi,top_left, bottom_right, 255, 2)

		write_image(oi, output_folder + meth)


def find_cursor():


	#Set 1 Images

	# for img_no in range(1,16):

	# 	original_image = cv2.imread('task3/pos_' + str(img_no) + '.jpg')

	# 	img_source = cv2.imread('task3/pos_' + str(img_no) + '.jpg',0)
		
	# 	#template = cv2.imread('task3/template.png',0)
	# 	template = cv2.imread('task3/temp.jpg',0)

		
	# 	laplacian_img = cv2.Laplacian(cv2.GaussianBlur(img_source, (3,3),0),cv2.CV_8U)
		
	# 	template = cv2.Laplacian(template,cv2.CV_8U)

		
	# 	output_folder = 'task3_set1/pos_' + str(img_no)

	# 	match_template(original_image, laplacian_img, template, output_folder)




	#Set 2 Images

	for sub_set in range(1,4):
		

		for img_no in range(1,7):

			original_image = cv2.imread('task3/task3_bonus/t' + str(sub_set) + '_' + str(img_no) + '.jpg')



			img_source =  cv2.imread('task3/task3_bonus/t' + str(sub_set) + '_' + str(img_no) + '.jpg', 0)
			
			#template = cv2.imread('task3/template.png',0)
			template = cv2.imread('task3/task3_bonus/t' + str(sub_set) + '_x.jpg',0)

			
			laplacian_img = cv2.Laplacian(cv2.GaussianBlur(img_source, (3,3),0),cv2.CV_8U)
			
			template = cv2.Laplacian(template,cv2.CV_8U)

			
			output_folder = 'task3_set2/t' + str(sub_set) + '/pos_' + str(img_no)

			match_template(original_image, laplacian_img, template, output_folder)














def main():

	#task 1

	task_1_img = cv2.imread("task1.png", 0)
	# edge_detection_x(task_1_img)
	# edge_detection_y(task_1_img)


	# #task 2

	task_2_img = cv2.imread("task2.jpg", 0)

	sigma_table = np.array([				
							[1/sqrt(2), 1, sqrt(2), 2, 2*sqrt(2)], 
							[sqrt(2), 2,  2*sqrt(2), 4, 4*sqrt(2)], 
							[2*sqrt(2), 4, 4*sqrt(2), 8, 8*sqrt(2)],
							[4*sqrt(2), 8, 8*sqrt(2), 16, 16*sqrt(2)]
						])


	

	#generate_octavs(task_2_img, sigma_table);

	# list = []
	# compute_DoG(list)
	# find_keypoints(task_2_img, list)


	find_cursor()














	






main()
 














