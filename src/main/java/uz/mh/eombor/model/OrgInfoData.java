package uz.mh.eombor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrgInfoData extends Auditable{
    private Date dateOfRegistration;
    private boolean status;
    private String registeringAuthority;
    private String stir;
    private String THSHT;
    private String DBIBT;
    private String IFUT;
    private String charterOfFoundations;
    private String email;
    private String phoneNumber;
    private String address;
    @OneToOne
    private OldStir oldStir;
}
