package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.NewCompanyRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String address;

    public Company(NewCompanyRequest ncr) {
        this.name = ncr.getName();
        this.address = ncr.getAddress();
    }
}
