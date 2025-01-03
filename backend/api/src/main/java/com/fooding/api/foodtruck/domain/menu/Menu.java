package com.fooding.api.foodtruck.domain.menu;

import com.fooding.api.foodtruck.domain.FoodTruck;
import com.fooding.api.foodtruck.exception.IllegalMenuNameException;
import com.fooding.api.foodtruck.exception.IllegalMenuPriceException;
import com.fooding.api.foodtruck.exception.MenuNameOverflowException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "img")
	private String img;

	@Column(name = "is_on_sale", nullable = false)
	private boolean onSale;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "foodtruck_id")
	private FoodTruck foodTruck;

	@Builder
	public Menu(String name, int price, String img, FoodTruck foodTruck) {
		this(name, price, img, true, foodTruck);
	}

	public Menu(String name, int price, String img, boolean onSale, FoodTruck foodTruck) {
		this.name = name;
		this.price = price;
		this.img = img;
		this.onSale = onSale;
		setFoodTruck(foodTruck);
	}

	private void setFoodTruck(FoodTruck foodTruck) {
		this.foodTruck = foodTruck;
		foodTruck.getMenuList().add(this);
	}

	public void enableSale() {
		this.onSale = true;
	}

	public void disableSale() {
		this.onSale = false;
	}

	public void update(String name, int price, String img) {
		checkNameValid(name);
		checkPriceValid(price);
		this.name = name;
		this.price = price;
		this.img = img;
	}

	private void checkNameValid(String name) {
		if (name == null) {
			throw new IllegalMenuNameException("Name cannot be null");
		}

		if (name.trim().isEmpty() || name.length() > 15) {
			throw new MenuNameOverflowException("Invalid name value");
		}
	}

	private void checkPriceValid(int price) {
		if (price < 0 || price > 9999999) {
			throw new IllegalMenuPriceException("Invalid price value");
		}
	}

}
