package seminar_test;


//*************** Client Program *************

class BST {
	public static void main(String args[]) {
		 Tree tr;
		 tr = new Tree(100);
		 tr.insert(50);
		 tr.insert(10);
		 tr.insert(70);
		 tr.insert(150);
		 tr.insert(200);
		 tr.insert(5);
		 tr.insert(500);
		 tr.print();
		}
}

class Tree { // Defines one node of a binary search tree
	
	public Tree(int n) {
		value = n;
		left = null;
		right = null;
	}
	public void print() {
		if (left != null) 
			left.print();
		System.out.println(value);
		if (right != null)
			right.print();
	}
	
	public int max() {
		if (right == null) 
			return value;
		else 
			return right.max();
	}
	
	public int min() {
		Tree temp = this;
		while(temp.left != null)
			temp = temp.left;
		return temp.value;
	}
	
	public void insert(int n) {
		if (value == n)
			return;
		if (value < n)
			if (right == null)
				right = new Tree(n);
			else
				right.insert(n);
		else if (left == null)
			left = new Tree(n);
		else
			left.insert(n);
	}

	protected int value;
	protected Tree left;
	protected Tree right;
}

























