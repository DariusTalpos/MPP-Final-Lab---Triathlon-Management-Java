package com.persistence.template;

import com.model.Round;
import org.springframework.stereotype.Component;

@Component
public interface IRoundRepo extends IGenericRepo<Long, Round> {
    public Round findRoundWithName(String name);
}
