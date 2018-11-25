UBIT = 'amlangup'
import numpy as np
import cv2
from matplotlib import pyplot as plt
import math


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


def apply_mask(img, _mask):

	padding_width = int((len(_mask[0])-1)/2)
	background_value =0


	padded_img = add_padding(image = img, padding_width = padding_width, background_value = background_value)

	h, w = padded_img.shape

	output_image = [[0 for col in range(w)] for row in range(h)]

	for i in range(padding_width, h-padding_width):
		for j in range(padding_width, w-padding_width):
			
			loop_end = (padding_width*2)+1

			sum = 0
			for x in range(0,loop_end):
				for y in range(0,loop_end):
					sum += _mask[x][y] * padded_img[i-padding_width+x][j-padding_width+y]

			output_image[i][j] = abs(sum)


	return remove_padding(image = np.asarray(output_image), padding_width = padding_width)

def generate_histogram(img):


	counter = [0 for col in range(256)]

	h, w = img.shape

	for i in range(h):
		for j in range(w):
			counter[img[i][j]] +=1

	#removing counts for 0 intensity pixels
	counter[0]=0

	plt.figure()

	plt.plot([col for col in range(256)], counter)
	plt.show()


def find_optimal_threshold(img, threshold, t_0 = 1, lowerbound =0):

	intensity_sum_left = 0
	count_left = 0

	intensity_sum_right = 0
	count_right = 0

	h, w = img.shape

	while True:

		for i in range(h):
			for j in range(w):
				if img[i][j] <= lowerbound:
					continue
				if img[i][j] > threshold:
					intensity_sum_right += img[i][j]
					count_right +=1

				else:
					intensity_sum_left+= img[i][j]
					count_left +=1


		mu_right = intensity_sum_right/count_right
		mu_left = intensity_sum_left/count_left


		threshold_new = int(0.5*(mu_left+mu_right))

		if threshold == threshold_new:
			break
		else:
			threshold = threshold_new

	
	return int(threshold)


def create_bounding_box(img):

	rec_img = cv2.cvtColor(img,cv2.COLOR_GRAY2RGB)


	cv2.rectangle(rec_img, (162, 124), (202, 164), (0, 255, 255), 2)
	cv2.rectangle(rec_img, (253, 75), (304, 207), (0, 0, 255), 2)
	cv2.rectangle(rec_img, (334, 25), (366, 287), (0, 255, 0), 2)
	cv2.rectangle(rec_img, (386, 38), (421, 254), (255 , 0, 0), 2)

	write_image(rec_img, 'res_segment')





def main():

	############# TASK 2 (a) ###########

	point_img = cv2.imread("original_imgs/point.jpg", 0)


	mask = [
				[-1, -1, -1],
				[-1,  8, -1],
				[-1, -1, -1]
			]


	masked_image = apply_mask(point_img, mask)
	

	
	# apply threshold

	h, w = masked_image.shape

	for i in range(h):
		for j in range(w):

			if masked_image[i][j] < 135:
				 masked_image[i][j] = 0



	op = masked_image/np.max(masked_image)

	op = op*255


	write_image(op, 'res_point')
	




	# ########## TASK 2 (b) ##########

	segment_img = cv2.imread("original_imgs/segment.jpg", 0)


	generate_histogram(segment_img)

	# From the histogram we can understand, since the intensity of chicken bones are brightest
	# So it's values should range from 190 to 255. We will be considering this values for finding threshold

	lowerbound = 185

	# Heuristic approach to get threshold T:
	# Intitializing estimation set to 230
	t = 220

	t = find_optimal_threshold(img = segment_img, threshold = t, t_0 = 0, lowerbound = lowerbound)


	h, w = segment_img.shape

	for i in range(h):
		for j in range(w):
			if segment_img[i][j] <t:
				segment_img[i][j] = 0
			else:
				segment_img[i][j] = 255



	create_bounding_box(segment_img)



main()