package tk.wlemuel.cotable.model;

import java.io.Serializable;
import java.util.List;

/**
 * ListEntity
 *
 * @author Steve Lemuel
 * @version 0.1
 * @desc ListEntity
 * @created 2015/05/09
 * @updated 2015/05/09
 */
public interface ListEntity extends Serializable {

    public List<?> getList();

}
