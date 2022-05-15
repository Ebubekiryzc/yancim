package tr.edu.duzce.mf.bm.yancim.dao.concretes;

import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.yancim.core.dao.concretes.HibernateMainDAO;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.RoomDAO;
import tr.edu.duzce.mf.bm.yancim.model.Room;

@Repository
public class HibernateRoomDAO extends HibernateMainDAO<Room> implements RoomDAO {
    public HibernateRoomDAO() {
        super(Room.class);
    }
}
