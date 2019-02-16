package seminar_test;

// Sorting programs written in a functionm-Oriented style
//		(for simple JIVE demo)


class Driver {

	public static void main(String args[]) {

		int a[] = { 90, 80, 70, 60, 50, 40, 30, 20, 10, 0 };

		// Sort.bubbleSort(a);

		Sort.quickSort(a);
	}
}

public class Sort {
	
	public static void bubbleSort(int a[]) {
		
		boolean flag = true;
		
		for (int j = a.length - 1; j > 0 && flag; j--) {
			flag = false;
			for (int i = 0; i < j; i++) {				
				if (a[i] > a[i + 1]) {
					swap(a, i, i+1);
					flag = true;
				}
			}
		}
	}

	static void swap(int a[], int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	
	public static void quickSort(int a[]) {
		qSort(a, 0, a.length - 1);
	}

	static void qSort(int a[], int start, int end) {
		int i = start;
		int k = end;
		if (end - start >= 1) {
			int pivot = a[start];
			while (k > i) {
				while (a[i] <= pivot && i <= end && k > i)
					i++;
				while (a[k] > pivot && k >= start && k >= i)
					k--;
				if (k > i)
					swap(a, i, k);
			}
			swap(a, start, k);
			qSort(a, start, k - 1); // quicksort left partition
			qSort(a, k + 1, end); // quicksort right partition
		} else
			return; // if only one element, no sorting
	}

}
