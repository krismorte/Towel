package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.towel.bean.Formatter;
import com.towel.el.annotation.Resolvable;

public class Person {
	private String name;
	private int age;
	private boolean live;
	@Resolvable(colName = "Birth", formatter = DateFormatter.class)
	private GregorianCalendar birth;
	private double money;
	private Person parent;

	public Person(final String name, final int age, final boolean live,
			final String birth) {
		this.name = name;
		this.age = age;
		this.live = live;
		this.birth = new GregorianCalendar();
		try {
			this.birth.setTime(new SimpleDateFormat("ddMMyyyy").parse(birth));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public Person(final String name, final int age, final boolean live) {
		this.name = name;
		this.age = age;
		this.live = live;
	}

	public Person(final double d) {
		this.age = (int) d;
	}

	public Person(final String str, final double d) {
		this.name = str;
		this.age = (int) d;
	}

	public Person() {
	}

	private class DateFormatter implements Formatter {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		@Override
		public String format(final Object arg0) {
			return format.format(((GregorianCalendar) arg0).getTime());
		}

		@Override
		public String getName() {
			return "date";
		}

		@Override
		public Object parse(final Object arg0) {
			GregorianCalendar cal = new GregorianCalendar();
			try {
				cal.setTime(format.parse(arg0.toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return cal;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(final int age) {
		this.age = age;
	}

	public void setLive(final boolean live) {
		this.live = live;
	}

	public boolean isLive() {
		return live;
	}

	@Override
	public String toString() {
		return "Name: " + name + " age: " + age;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(final double money) {
		this.money = money;
	}

	public void setParent(final Person parent) {
		this.parent = parent;
	}

	public Person getParent() {
		return parent;
	}

	public void printAttrs() {
		System.out.println("Name: " + getName());
		System.out.println("Age: " + getAge());
		System.out.println("Live?: " + isLive());
	}
}
