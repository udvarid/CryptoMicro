package com.donat.crypto.reporter.repository;

import java.util.List;

import com.donat.crypto.reporter.domain.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CandleRepository extends JpaRepository<Candle, Long> {

    @Query(value = "SELECT t.* FROM (SELECT c.* FROM candles as c WHERE c.currency_pair = ?1 "
            + "ORDER BY c.time DESC LIMIT ?2) as t ORDER BY t.time ASC",
            nativeQuery = true)
    List<Candle> getAllByCurrencyPair(String currencyPair, Integer lastNumber);

    @Query(value = "select c0.*"
            + "from candles c0 inner join (select c.currency_pair,"
            + "                                   max(c.time) max_time"
            + "                            from candles c"
            + "                            group by c.currency_pair) c1"
            + "                on c0.currency_pair = c1.currency_pair and c0.time = c1.max_time",
            nativeQuery = true)
    List<Candle> getRecentCurrencyPairs();

}