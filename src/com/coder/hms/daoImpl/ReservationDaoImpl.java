/**
 * @author Coder ACJHP
 * @Email hexa.octabin@gmail.com
 * @Date 15/07/2017
 */
package com.coder.hms.daoImpl;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.coder.hms.connection.DataSourceFactory;
import com.coder.hms.dao.ReservationDAO;
import com.coder.hms.entities.Reservation;

public class ReservationDaoImpl implements ReservationDAO{

	private Session session;
	private DataSourceFactory dataSourceFactory;
	private final Logger LOGGER = Logger.getLogger(ReservationDaoImpl.class.getName());
	
	public ReservationDaoImpl() {
		dataSourceFactory = new DataSourceFactory();
		DataSourceFactory.createConnection();
		
	}
	
	@Override
	public Reservation findReservationById(long theId) {
		session = dataSourceFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query<Reservation> query = session.createQuery("from Reservation where id=:theId", Reservation.class);
		query.setParameter("theId", theId);
		Reservation reservation = query.getSingleResult();
		
		LOGGER.info(reservation.toString());
		
		session.close();
		return reservation;
	}

	@Override
	public Reservation findReservationByDate(String Date) {
		session = dataSourceFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query<Reservation> query = session.createQuery("from Reservation where checkinDate=:Date", Reservation.class);
		query.setParameter("Date", Date);
		Reservation reservation = query.getSingleResult();
		
		LOGGER.info(reservation.toString());
		
		session.close();
		return reservation;
	}

	@Override
	public void saveReservation(Reservation reservation) {
		session = dataSourceFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(reservation);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void cancelReservation(long reservationId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Reservation> getAllReservations() {
		session = dataSourceFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query<Reservation> query = session.createQuery("from Reservation", Reservation.class);
		List<Reservation> reservList = query.getResultList();
		
		LOGGER.info(reservList.toString());
		
		session.close();
		
		return reservList;
	}

	public List<Reservation> getReservsByDate(String today) {
		session = dataSourceFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query<Reservation> query = session.createQuery("from Reservation where checkinDate=:today", Reservation.class);
		query.setParameter("today", today);
		List<Reservation> reservList = query.getResultList();
		
		LOGGER.info(reservList.toString());
		
		session.close();
		return reservList;
	}

	public List<Reservation> getGaranteedReservs(String today) {
		session = dataSourceFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query<Reservation> query = session.createQuery("from Reservation "
							+ "where bookStatus = 'GUARANTEE' and checkinDate=:today", Reservation.class);
		query.setParameter("today", today);
		List<Reservation> guaranteedList = query.getResultList();
		session.close();
		return guaranteedList;
	}

	public List<Reservation> getReservsAsWaitlist(String today) {
		session = dataSourceFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query<Reservation> query = session.createQuery("from Reservation "
							+ "where bookStatus = 'WAITLIST' and checkinDate=:today", Reservation.class);
		query.setParameter("today", today);
		List<Reservation> waitedList = query.getResultList();
		session.close();
		return waitedList;
	}

	public Reservation getLastReservation() {
		session = dataSourceFactory.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Query<Reservation> query = session.createQuery("from Reservation order by Id DESC", Reservation.class);
		query.setMaxResults(1);
		Reservation lastRecord = query.getSingleResult();
		session.close();
		return lastRecord;
	}

}
