package model;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import container.CollidableContainer;
import container.DrawContainer;
import container.MoveContainer;

public class Handler {

	private final ArrayList<GameObject> objects;
	private final LinkedList<MouseEvent> input;

	private final DrawContainer drawContainer;
	private final MoveContainer moveContainer;
	private final CollidableContainer collidableContainer;

	public int score;

	public Handler() {
		objects = new ArrayList<GameObject>();
		input = new LinkedList<MouseEvent>();
		drawContainer = new DrawContainer();
		moveContainer = new MoveContainer();
		collidableContainer = new CollidableContainer(this);
	}

	public void tick(final double delta) {
		moveContainer.update(delta);
	}

	public void render(final Graphics2D g2d) {
		drawContainer.update(g2d);
	}

	public void handleInput() {
		while (input.size() > 0) {
			collidableContainer.update(input);
			input.poll();
		}
	}

	public void addObject(final GameObject object) {
		this.objects.add(object);
	}

	public void removeObject(final GameObject object) {
		this.objects.remove(object);
	}

	public ArrayList<GameObject> getObjectList() {
		return objects;
	}

	public MoveContainer getMoveContainer() {
		return moveContainer;
	}

	public DrawContainer getDrawContainer() {
		return drawContainer;
	}

	public CollidableContainer getCollidableContainer() {
		return collidableContainer;
	}

	public void addInput(final MouseEvent e) {
		input.add(e);
	}

	public LinkedList<MouseEvent> getInput() {
		return input;
	}
}
