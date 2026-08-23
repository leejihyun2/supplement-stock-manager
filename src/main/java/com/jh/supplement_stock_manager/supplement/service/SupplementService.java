package com.jh.supplement_stock_manager.supplement.service;

import com.jh.supplement_stock_manager.supplement.dto.SupplementCreateRequest;
import com.jh.supplement_stock_manager.supplement.dto.SupplementCreateResponse;
import com.jh.supplement_stock_manager.supplement.entity.Supplement;
import com.jh.supplement_stock_manager.supplement.repository.SupplementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplementService {
    private static final Long MVP_USER_ID =1L;

    private final SupplementRepository supplementRepository;

    public SupplementCreateResponse createSupplement(
            SupplementCreateRequest request
    ){
        Supplement supplement = new Supplement(
                MVP_USER_ID,
                request.supplementName(),
                request.totalQuantity(),
                request.currentStock(),
                request.unopenedBottleCount(),
                request.servingSize(),
                request.servingsPerDay(),
                request.intakeSchedule()
        );
        Supplement savedSupplement = supplementRepository.save(supplement);
        return new SupplementCreateResponse(
                savedSupplement.getSupplementId(),
                "영양제가 등록되었습니다."

        );
    }
}
