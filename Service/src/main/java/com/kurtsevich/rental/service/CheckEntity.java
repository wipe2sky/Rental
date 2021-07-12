package com.kurtsevich.rental.service;

import com.kurtsevich.rental.Status;
import com.kurtsevich.rental.api.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckEntity {
    public Boolean checkIsActive(Status... status) {
        for (Status stat :
                status) {
            if (!Status.ACTIVE.equals(stat)) {
                log.warn("IN CheckEntity:checkIsActive - check status is not active");
                throw new ServiceException("Status is not active");
            }
        }
        return true;
    }
}
