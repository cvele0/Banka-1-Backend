package rs.edu.raf.banka1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class OptionsModel extends ListingBase implements Serializable {
    @Column
    private String optionType;
    @Column
    private Double strikePrice;
    @Column
    private String currency;
    @Column
    private Double impliedVolatility;
    @Column
    private Integer openInterest;
    @Column
    private Long expirationDate;
}
