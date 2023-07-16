package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.dto.customer.CustomerResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class TransferCreResDTO {
    private CustomerResDTO sender;
    private CustomerResDTO recipient;

}
