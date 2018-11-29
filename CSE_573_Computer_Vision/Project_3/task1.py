UBIT = 'amlangup'
import numpy as np
import cv2


OUTPUT_FOLDER = 'output/'


def print_image(img, image_name = 'test'):
	cv2.namedWindow("show Image", cv2.WINDOW_NORMAL)
	cv2.imshow("show image", img)
	cv2.waitKey(0)
	cv2.destroyAllWindows()
	

def write_image(img, image_name = 'test'):
	cv2.imwrite(OUTPUT_FOLDER + image_name + '.jpg',img)


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




def apply_dilation(img, _structuring_element):

	padding_width = int((len(_structuring_element[0])-1)/2)
	background_value =0



	padded_img = add_padding(image = img, padding_width = padding_width, background_value = background_value)

	h, w = padded_img.shape

	output_image = [[0 for col in range(w)] for row in range(h)]

	for i in range(padding_width, h-padding_width):
		for j in range(padding_width, w-padding_width):
			
			loop_end = (padding_width*2)+1

			matchFound = False
			for x in range(0,loop_end):
				for y in range(0,loop_end):
					if _structuring_element[x][y] == 1 and int(padded_img[i-padding_width+x][j-padding_width+y]) ==_structuring_element[x][y]:
						output_image[i][j] = 1
						matchFound = True
						break

				if matchFound:
					break

	return remove_padding(image = np.asarray(output_image), padding_width = padding_width)



def apply_erosion(img, _structuring_element):

	padding_width = int((len(_structuring_element[0])-1)/2)
	background_value =0



	padded_img = add_padding(image = img, padding_width = padding_width, background_value = background_value)

	h, w = padded_img.shape

	output_image = [[0 for col in range(w)] for row in range(h)]

	for i in range(padding_width, h-padding_width):
		for j in range(padding_width, w-padding_width):
			
			loop_end = (padding_width*2)+1

			matchFound = True
			for x in range(0,loop_end):
				for y in range(0,loop_end):
					if _structuring_element[x][y] == 1 and int(padded_img[i-padding_width+x][j-padding_width+y]) !=_structuring_element[x][y]:
						matchFound = False
						break

				if not matchFound:
					break

			if matchFound:
					output_image[i][j] = 1

	return remove_padding(image = np.asarray(output_image), padding_width = padding_width)




def main():

	noise_img = cv2.imread("original_imgs/noise.jpg", 0)

	# noise.jpg only has values 0 and 255. Converting it to binary image.
	noise_img_bin = noise_img//255

	structuring_element = 	[
								[1, 0, 0, 0, 1],
								[0, 1, 0, 1, 0],
								[0, 0, 1, 0, 0],
								[0, 1, 0, 1, 0],
								[1, 0, 0, 0, 1]
							]

	######### Task 1 (a) (b) ##########


	# the below methods will work for any n x n stucturing element 
	# where n is an odd number and origin in the middle

	# closing-opening

	step1 = apply_dilation(noise_img_bin, structuring_element)

	step2 = apply_erosion(step1, structuring_element)

	step3 = apply_erosion(step2, structuring_element)

	res_noise1 = apply_dilation(step3, structuring_element)

	write_image(res_noise1*255 , 'res_noise1')

	# opening- closing

	step1 = apply_erosion(noise_img_bin, structuring_element)

	step2 = apply_dilation(step1, structuring_element)

	step3 = apply_dilation(step2, structuring_element)

	res_noise2 = apply_erosion(step3, structuring_element)

	write_image(res_noise2*255, 'res_noise2')


	######### Task 1 (c) ##########

	dilated_img = apply_dilation(res_noise1, structuring_element)
	eroded_img = apply_erosion(res_noise1, structuring_element)

	res_bound1 = dilated_img - eroded_img

	write_image(res_bound1*255, 'res_bound1')


	dilated_img = apply_dilation(res_noise2, structuring_element)
	eroded_img = apply_erosion(res_noise2, structuring_element)

	res_bound2 = dilated_img - eroded_img

	write_image(res_bound2*255, 'res_bound2')






main()