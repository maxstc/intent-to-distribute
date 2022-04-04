package model;

public class Person {

	private String name;
	private Date birthday;

	private int talent;
	private int charisma;
	private int guile;
	private int scholarship;
	
	public Person(String name) {
		this.name = name;
		generateSkills();
	}
	
	private void generateSkills() {
		talent = (int) (10 * (Math.random() + Math.random()));
		charisma = (int) (10 * (Math.random() + Math.random()));
		guile = (int) (10 * (Math.random() + Math.random()));
		scholarship = (int) (10 * (Math.random() + Math.random()));
	}
	
}
