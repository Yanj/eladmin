package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveResourceGroupCount implements Serializable {

    private String date;

    private Long workTimeId;

    private Long resourceGroupId;

    private long count;

}
