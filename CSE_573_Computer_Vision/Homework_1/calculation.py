import numpy as np



template = np.array([[3,10,20],[18,1,5],[2,30,3]])
# img = np.array([[75,127,52],[87,86,0],[12,188,176]])
# img = np.array([[3,9,208],[1,2,6],[22,40,9]])
img = np.array([[0,0,0],[72,0,84],[170,26,54]])

#diff = img - template;

up = np.sum((template - np.mean(template)) * (img - np.mean(img)))
down = np.sqrt(np.sum(np.square(template - np.mean(template))) * np.sum(np.square(img - np.mean(img))))



print(up)
print(down)
print(up/down)