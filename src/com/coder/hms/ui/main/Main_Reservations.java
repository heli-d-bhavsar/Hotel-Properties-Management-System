/**
 * @author Coder ACJHP
 * @Email hexa.octabin@gmail.com
 * @Date 15/07/2017
 */
package com.coder.hms.ui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;

import com.coder.hms.beans.LocaleBean;
import com.coder.hms.daoImpl.CustomerDaoImpl;
import com.coder.hms.daoImpl.HotelDaoImpl;
import com.coder.hms.daoImpl.HotelSystemStatusImpl;
import com.coder.hms.daoImpl.PaymentDaoImpl;
import com.coder.hms.daoImpl.ReservationDaoImpl;
import com.coder.hms.daoImpl.RoomDaoImpl;
import com.coder.hms.entities.Customer;
import com.coder.hms.entities.Hotel;
import com.coder.hms.entities.HotelSystemStatus;
import com.coder.hms.entities.Payment;
import com.coder.hms.entities.Reservation;
import com.coder.hms.entities.Room;
import com.coder.hms.ui.external.AllReservationsWindow;
import com.coder.hms.ui.external.InformationFrame;
import com.coder.hms.ui.external.NewReservationWindow;
import com.coder.hms.ui.external.UpdateReservationWindow;
import com.coder.hms.ui.extras.*;
import com.coder.hms.utils.ChangeComponentOrientation;
import com.coder.hms.utils.LoggingEngine;
import com.coder.hms.utils.ResourceControl;
import com.toedter.calendar.JDateChooser;

public class Main_Reservations extends JPanel {

    /**
     *
     */
    private JTable table;
    private Date convertedDate;
    private JPanel buttonPanel;
    private LocalDate reservDate;
    private JTextField refNoField;
    private final LocaleBean bean;
    private ResourceBundle bundle;
    private JScrollPane scrollPane;
    private RoomDaoImpl roomDaoImpl;
    private DefaultTableModel model;
    private JTextField agencyRefField;
    private HotelDaoImpl hotelDoaImpl;
    private static LoggingEngine logging;
    private PaymentDaoImpl paymentDaoImpl;
    private CustomerDaoImpl customerDaoImpl;
    private NewReservationWindow newReservationEx;
    private ReservationDaoImpl reservationDaoImpl;
    private final HotelSystemStatus systemStatus;
    private static final long serialVersionUID = 1L;
    private JButton newRezBtn, findBtn, showAllReservs;
    private JDateChooser startDatePicker, endDatePicker;
    private ChangeComponentOrientation componentOrientation;
    private JLabel startdateLbl, endDateLbl, referansNoLbl, agencyRefLbl;
    private final HotelSystemStatusImpl statusImpl = new HotelSystemStatusImpl();
    private final String[] rezColsName = {"DATE", "CAPASITE ", "FULL ", "EMPTY", "GARANTED", "WAITING"};
    private final CustomTableHeaderRenderer THR = new CustomTableHeaderRenderer();
    AbstractFactory tableRenderFactory = new TableRendererFactory();
    private final ReservationTableRenderer customTCR = (ReservationTableRenderer) tableRenderFactory.getTableRenderer("RESERVATION TABLE");

    public Main_Reservations() {

        logging = LoggingEngine.getInstance();

        bean = LocaleBean.getInstance();
        componentOrientation = new ChangeComponentOrientation();
        componentOrientation.setThePanel(this);

        setLayout(new BorderLayout(0, 0));

        buttonPanel = new JPanel();
        buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        buttonPanel.setAutoscrolls(true);
        buttonPanel.setPreferredSize(new Dimension(10, 65));
        add(buttonPanel, BorderLayout.NORTH);

        newRezBtn = new JButton("New Reservation");
        newRezBtn.setBounds(6, 12, 155, 45);
        newRezBtn.setIcon(new ImageIcon(Main_Blockade.class.getResource("/com/coder/hms/icons/main_new_rez.png")));
        newRezBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        newRezBtn.setPreferredSize(new Dimension(150, 33));
        newRezBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
        newRezBtn.setFont(new Font("Arial", Font.BOLD, 12));
        newRezBtn.addActionListener(ActionListener -> {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    newReservationEx = new NewReservationWindow();
                    newReservationEx.setVisible(true);
                }
            });
        });
        buttonPanel.setLayout(null);
        buttonPanel.add(newRezBtn);

        final JSeparator separator = new JSeparator();
        separator.setBackground(Color.DARK_GRAY);
        separator.setBounds(175, 12, 13, 45);
        separator.setOrientation(SwingConstants.VERTICAL);
        separator.setFocusable(true);
        separator.setForeground(Color.DARK_GRAY);
        separator.setAutoscrolls(true);
        separator.setPreferredSize(new Dimension(10, 20));
        buttonPanel.add(separator);

        startdateLbl = new JLabel("Start date : ");
        startdateLbl.setBounds(192, 8, 79, 26);
        buttonPanel.add(startdateLbl);

        systemStatus = statusImpl.getSystemStatus();
        convertedDate = Date.from(systemStatus.getDateTime().atStartOfDay(ZoneId.systemDefault()).toInstant());

        startDatePicker = new JDateChooser();
        startDatePicker.setDate(convertedDate);
        startDatePicker.setDateFormatString("yyyy-MM-dd");
        startDatePicker.setBounds(275, 8, 155, 26);
        buttonPanel.add(startDatePicker);

        endDateLbl = new JLabel("End date : ");
        endDateLbl.setBounds(192, 35, 79, 26);
        buttonPanel.add(endDateLbl);

        endDatePicker = new JDateChooser();
        endDatePicker.setDate(convertedDate);
        endDatePicker.setDateFormatString("yyyy-MM-dd");
        endDatePicker.setBounds(275, 35, 155, 26);
        buttonPanel.add(endDatePicker);

        referansNoLbl = new JLabel("Referans No : ");
        referansNoLbl.setBounds(442, 6, 94, 26);
        buttonPanel.add(referansNoLbl);

        agencyRefLbl = new JLabel("Agency Ref : ");
        agencyRefLbl.setBounds(442, 33, 94, 26);
        buttonPanel.add(agencyRefLbl);

        findBtn = new JButton("Search");
        findBtn.setIcon(new ImageIcon(Main_Reservations.class.getResource("/com/coder/hms/icons/main_find.png")));
        findBtn.setPreferredSize(new Dimension(150, 33));
        findBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        findBtn.setFont(new Font("Arial", Font.BOLD, 12));
        findBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
        findBtn.setBounds(690, 8, 114, 48);
        findBtn.addActionListener(findRezervation());
        buttonPanel.add(findBtn);

        refNoField = new JTextField();
        refNoField.setBounds(535, 6, 143, 26);
        refNoField.setFont(new Font("Arial", Font.BOLD, 13));
        refNoField.setColumns(10);
        buttonPanel.add(refNoField);

        agencyRefField = new JTextField();
        agencyRefField.setBounds(535, 33, 143, 26);
        agencyRefField.setFont(new Font("Arial", Font.BOLD, 13));
        agencyRefField.setColumns(10);
        buttonPanel.add(agencyRefField);
        
        showAllReservs = new JButton("Show All");
        showAllReservs.addActionListener(ActionListener -> {
        	SwingUtilities.invokeLater(AllReservationsWindow::new);
        });
        showAllReservs.setIcon(new ImageIcon(Main_Reservations.class.getResource("/com/coder/hms/icons/reservation_allreservs.png")));
        showAllReservs.setPreferredSize(new Dimension(150, 33));
        showAllReservs.setHorizontalTextPosition(SwingConstants.RIGHT);
        showAllReservs.setFont(new Font("Arial", Font.BOLD, 12));
        showAllReservs.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
        showAllReservs.setBounds(827, 8, 155, 45);
        buttonPanel.add(showAllReservs);

        model = new DefaultTableModel(rezColsName, 0);

        customTCR.setHorizontalAlignment(SwingConstants.CENTER);
        THR.setHorizontalAlignment(SwingConstants.CENTER);

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
        table.getTableHeader().setDefaultRenderer(THR);
        table.setDefaultRenderer(Object.class, customTCR);
        table.setFont(new Font("Dialog", Font.PLAIN, 14));
        table.setBackground(UIManager.getColor("InternalFrame.borderColor"));

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        add(scrollPane, BorderLayout.CENTER);

        getReadyForDataFlow();

        changeLanguage(bean.getLocale());
        //change component orientation with locale.
        if (bean.getLocale().toString().equals("ar_IQ")) {
            componentOrientation.changeOrientationOfJPanelToRight();
        } else {
            componentOrientation.changeOrientationOfJPanelToLeft();
        }

    }

    private void changeLanguage(Locale locale) throws MissingResourceException {

        bundle = ResourceBundle.getBundle("com/coder/hms/languageFiles/LocalizationBundle", locale, new ResourceControl());

        this.newRezBtn.setText(bundle.getString("NewReservation"));
        this.showAllReservs.setText(bundle.getString("AllReservations"));
        this.findBtn.setText(bundle.getString("Search"));
        this.revalidate();
        this.repaint();
    }

    private float calcFullnessPersentage(int count, int capasite) {
        float persentage = (100 * count) / capasite;
        return persentage;
    }

    public synchronized void getReadyForDataFlow() {

        roomDaoImpl = new RoomDaoImpl();
        hotelDoaImpl = new HotelDaoImpl();
        paymentDaoImpl = new PaymentDaoImpl();
        customerDaoImpl = new CustomerDaoImpl();
        reservationDaoImpl = new ReservationDaoImpl();
    }

    public void populateMainTable() {

        model.setRowCount(0);

        reservDate = systemStatus.getDateTime();
        reservDate = reservDate.minusDays(1);

        //get hotel capasite.
        final Hotel hotel = hotelDoaImpl.getHotel();

        for (int i = 0; i < 31; i++) {

            reservDate = reservDate.plusDays(1);
            String today = reservDate.toString();

            List<Reservation> reservList = reservationDaoImpl.getReservListByThisDate(today);
            List<Reservation> garanteedReservList = reservationDaoImpl.getGaranteedReservs(today);
            List<Reservation> reservsAsWaitList = reservationDaoImpl.getReservsAsWaitlist(today);

            final float fullnesPersentage = calcFullnessPersentage(reservList.size(), hotel.getRoomCapacity());
            final float emptyPersentage = 100f - fullnesPersentage;

            final Object[] colRowVect = new Object[]{today, hotel.getRoomCapacity(), fullnesPersentage + "%",
                emptyPersentage + "%", garanteedReservList.size(), reservsAsWaitList.size()};
            model.addRow(colRowVect);
        }

    }

	public ActionListener findRezervation() {

		final ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				final UpdateReservationWindow reservationPane = new UpdateReservationWindow();

				String customerCountry = "";
				Reservation theReservation = null;

				if (agencyRefField.getText().length() > 0) {

					logging.setMessage(
							"Searching reservation with AGENCY REFERANCE NUMBER : " + agencyRefField.getText());

					theReservation = reservationDaoImpl.findReservationByAgencyRefNo(agencyRefField.getText());

				} else if (refNoField.getText().length() > 0) {

					logging.setMessage("Searching reservation by REFERANCE NO : " + refNoField.getText());

					theReservation = reservationDaoImpl.findReservationByRefNo(refNoField.getText());

				} else {

					// get dates from date pickers
					final LocalDate startDate = startDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					final LocalDate endDate = endDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

					logging.setMessage("Searching reservation by DATE : " + startDate + ":" + endDate);

					// compare if start date greater than end date
					if (startDate.isAfter(endDate)) {
						JOptionPane.showMessageDialog(null, "Start date is after end date!",
								JOptionPane.MESSAGE_PROPERTY, JOptionPane.WARNING_MESSAGE);
					} // or both is same date
					else if (startDate.equals(endDate)) {
						JOptionPane.showMessageDialog(null,
								"Start date equals end date!\nPlease be sure you're choose right date.",
								JOptionPane.MESSAGE_PROPERTY, JOptionPane.WARNING_MESSAGE);
					} 
					else {

						theReservation = reservationDaoImpl.findSingleReservByThisDate(startDate.toString());
						logging.setMessage("Reservation found : " + theReservation.toString());
					}
				}

				if (theReservation != null) {

					final List<Customer> customerList = customerDaoImpl.getCustomerByReservId(theReservation.getId());
					final Room room = roomDaoImpl.getRoomByReservId(theReservation.getId());

					reservationPane.setRezIdField(theReservation.getId());
					reservationPane.setNameSurnameField(theReservation.getGroupName());
					reservationPane.setCheckinDate(theReservation.getCheckinDate());
					reservationPane.setCheckoutDate(theReservation.getCheckoutDate());
					reservationPane.setTotalDaysField(theReservation.getTotalDays());
					reservationPane.setReservNote(theReservation.getNote());
					reservationPane.setAgency(theReservation.getAgency());
					reservationPane.setHostType(theReservation.getHostType());
					reservationPane.setCreditType(theReservation.getCreditType());
					reservationPane.setReservStatus(theReservation.getBookStatus());
					reservationPane.setReferanceNo(theReservation.getReferanceNo());
					reservationPane.setAgencyRefNo(theReservation.getAgencyRefNo());
					reservationPane.setRoomNumber(room.getNumber());
					reservationPane.setRoomType(room.getType());
					reservationPane.setPersonCountSpinner(room.getPersonCount());
					reservationPane.setPriceOfRoom(room.getPrice());
					reservationPane.setCurrency(room.getCurrency());
					reservationPane.setCustomerCountry(customerCountry);

					reservationPane.setRoomCountTableRows(new Object[] { room.getNumber(), room.getType(),
							room.getPersonCount(), room.getPrice(), room.getCurrency() });

					List<Object[]> objList = new ArrayList<>();
					for (Customer cst : customerList) {
						if (cst.getReservationId() == theReservation.getId()) {
							customerCountry = cst.getCountry();
							objList.add(
									new Object[] { room.getNumber(), room.getType(), cst.getFirstName(), cst.getLastName() });
							reservationPane.setRoomInfoTableRows(objList);
						}
					}

					if (theReservation.getPaymentStatus()) {

						Payment payment = paymentDaoImpl.getEarlyPaymentByRoomNumber(room.getNumber());
						reservationPane.setEarlyPaymetTableRows(
								new Object[] { payment.getTitle(), payment.getPaymentType(), payment.getPrice(),
										payment.getCurrency(), payment.getExplanation(), payment.getDateTime() });
						final InformationFrame infoFrame = new InformationFrame();
						infoFrame.setMessage("Early payment : " + payment.getPrice() + payment.getCurrency());
						infoFrame.setVisible(true);
					}

					reservationPane.setVisible(true);
				}
			}
		};
		return listener;
	}
}
