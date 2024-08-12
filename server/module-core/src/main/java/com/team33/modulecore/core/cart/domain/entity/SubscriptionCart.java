package com.team33.modulecore.core.cart.domain.entity;

import java.util.Objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.proxy.HibernateProxy;

import com.team33.modulecore.core.item.domain.entity.Item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("subscription")
@Entity
public class SubscriptionCart extends Cart {

	public static SubscriptionCart create() {
		return new SubscriptionCart();
	}

	public void addSubscriptionItem(CartItem cartItem) {
		cartItem.addCart(this);
		super.cartItems.add(cartItem);

		Item item = cartItem.getItem();
		super.price = super.price.addPriceInfo(
			item.getRealPrice(),
			item.getDiscountPrice(),
			cartItem.getTotalQuantity()
		);
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ?
			((HibernateProxy)o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
			((HibernateProxy)this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass)
			return false;
		SubscriptionCart that = (SubscriptionCart)o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ?
			((HibernateProxy)this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
			getClass().hashCode();
	}
}
