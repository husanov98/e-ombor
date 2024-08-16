package uz.mh.eombor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.mh.eombor.enums.TransportType;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NewClientInfo extends Auditable{
    private String TEBNH;
    private String transportNumber;
    private Float Brutto;
    private String receiver;
    private String stir;
    private Integer post;
    private Date deliveryDate;
    private String placeOfArrival;
    private boolean isAllowed;
    @Enumerated(EnumType.STRING)
    private TransportType transportType;
    private Timestamp solvedTime;
}
