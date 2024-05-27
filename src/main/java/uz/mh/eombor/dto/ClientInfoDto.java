package uz.mh.eombor.dto;

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
public class ClientInfoDto {
    private Long id;
    private String TEBNH;
    private String transportNumber;
    private Float Brutto;
    private String receiver;
    private String stir;
    private Integer post;
    private Date deliveryDate;
    private String placeOfArrival;
    private boolean isAllowed;
    private TransportType transportType = TransportType.TRUCK;
    private Timestamp solvedTime;
}
