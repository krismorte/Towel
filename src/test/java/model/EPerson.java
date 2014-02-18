package model;

public class EPerson extends Person {
	private String superPower;

	public EPerson(final String str, final double d) {
		super(str, d);
	}

	public void setSuperPower(final String superPower) {
		this.superPower = superPower;
	}

	public String getSuperPower() {
		return superPower;
	}
}
