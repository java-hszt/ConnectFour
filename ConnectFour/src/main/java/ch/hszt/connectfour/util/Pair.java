/**
 * 
 */
package ch.hszt.connectfour.util;

/**
 * Generic helper class enabling to store a pair objects
 * @author Markus Vetsch
 * @version 1.0, 31.10.2011
 * @param T1 - The type of the first object.
 * @param T2 - The type of the second object.
 */
public class Pair<T1, T2>
{
	private T1 left;
	private T2 right;
	
	public Pair(T1 left, T2 right)
	{
		this.left = left;
		this.right = right;
	}
	
	public T1 getLeft()
	{
		return left;
	}
	
	public void setLeft(T1 left)
	{
		this.left = left;
	}
	
	public T2 getRight()
	{
		return right;
	}
	
	public void setRight(T2 right)
	{
		this.right = right;
	}
}
