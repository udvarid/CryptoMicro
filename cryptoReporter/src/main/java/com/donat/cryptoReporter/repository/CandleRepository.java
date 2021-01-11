package com.donat.cryptoReporter.repository;

import com.donat.cryptoReporter.domain.Candle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandleRepository extends JpaRepository<Candle, Long> {

}