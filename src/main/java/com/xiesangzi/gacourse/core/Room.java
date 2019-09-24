package com.xiesangzi.gacourse.core;

/**
 * Simple Room abstraction -- used to store the room capacity and compare against the student Clazz's size.
 * 用于存储教室容量并与学生组的大小进行比较。
 */
public class Room {
	private final int roomId;
	private final String roomNumber;
	private final int capacity;

	/**
	 * Initialize new Room
	 * 
	 * @param roomId
	 *            The ID for this classroom
	 * @param roomNumber
	 *            The room number
	 * @param capacity
	 *            The room capacity
	 */
	public Room(int roomId, String roomNumber, int capacity) {
		this.roomId = roomId;
		this.roomNumber = roomNumber;
		this.capacity = capacity;
	}

	/**
	 * Return roomId
	 * 
	 * @return roomId
	 */
	public int getRoomId() {
		return this.roomId;
	}

	/**
	 * Return room number
	 * 
	 * @return roomNumber
	 */
	public String getRoomNumber() {
		return this.roomNumber;
	}

	/**
	 * Return room capacity
	 * 
	 * @return capacity
	 */
	public int getRoomCapacity() {
		return this.capacity;
	}
}