//Nathaniel Simmons, nis190000
public class Node <T>{
	// this class has 4 pointers to other nodes so it can be linked in a 2d grid, as well as a payload that can hold a class with information on the node.
	// it is also generic, so the information held can be any kind of object.
	private T payload;
	private Node<T> left;
	private Node<T> right;
	private Node<T> up;
	private Node<T> down;
	public Node(){
		payload = null;
		left = null;
		right = null;
		up = null;
		down = null;
	}
	public T getSeat() {
		return payload;
	}
	public void setSeat(T payload) {
		this.payload = payload;
	}
	public Node<T> getLeft() {
		return left;
	}
	public void setLeft(Node<T> left) {
		this.left = left;
	}
	public Node<T> getRight() {
		return right;
	}
	public void setRight(Node<T> right) {
		this.right = right;
	}
	public Node<T> getUp() {
		return up;
	}
	public void setUp(Node<T> up) {
		this.up = up;
	}
	public Node<T> getDown() {
		return down;
	}
	public void setDown(Node<T> down) {
		this.down = down;
	}
}