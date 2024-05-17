package com.team33.modulecore.item.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.team33.modulecore.item.domain.entity.Item;

/**
 * The interface Item command repository.
 */
public interface ItemCommandRepository extends Repository<Item, Long> {

	Item save(Item item);

	Optional<Item> findById(long id);

	void saveAll(Iterable<Item> entities);

	/**
	 * 아이템 수량을 1 증가시켜 업데이트 합니다.
	 *
	 * @param itemId the item id
	 * @return the item
	 */
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Item i SET i.statistics.view = i.statistics.view + 1L WHERE i.id = :itemId")
	Item incrementView(@Param("itemId") Long itemId);

	/**
	 * 아이템 판매량을 1 증가시켜 업데이트합니다.
	 *
	 * @param itemId the item id
	 */
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE Item i SET i.statistics.sales = i.statistics.sales + 1 WHERE i.id = :itemId")
	Long incrementSales(@Param("itemId") Long itemId);
}

