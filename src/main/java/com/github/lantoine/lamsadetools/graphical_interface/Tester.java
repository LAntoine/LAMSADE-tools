package com.github.lantoine.lamsadetools.graphical_interface;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lantoine.lamsadetools.conferences.Conference;
import com.github.lantoine.lamsadetools.conferences.database.ConferenceDatabase;
import com.github.lantoine.lamsadetools.map.AddressInfos;
import com.github.lantoine.lamsadetools.map.GoogleItineraryMap;
import com.github.lantoine.lamsadetools.utils.Util;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ValidationException;

/**
 * The Tester class is the interface of the LAMSADE-Tools application It allows
 * the user : -to see all the existing conferences, -to add new ones -to see the
 * itinerary on openStreetMap
 */
public class Tester {
	private static final Logger LOGGER = LoggerFactory.getLogger(Tester.class);

	/**
	 * Converts a LocalDate passed by parameter into a string
	 *
	 * @param localdate
	 *            the date to be converted
	 * @return
	 */
	private static String convertLocaldateToString(LocalDate localdate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/LLLL/yyyy");
		String formattedDate = localdate.format(formatter);
		return formattedDate;
	}

	/**
	 * fillConferenceTable takes a table in parameter that will be filled with
	 * all the conferences information
	 *
	 * @param table
	 * @throws SQLException
	 */
	private static void fillConferenceTable(Table table) throws SQLException {
		String[] titles = { "Title", "URL", "Start Date", "End Date", "Fee", "City", "Address"};
		ArrayList<Conference> confs = ConferenceDatabase.returnAllConferencesFromDatabase();

		for (String title : titles) {
			System.out.println(title);
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(title);
		}

		for (Conference i : confs) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0, i.getTitle());
			System.out.println(item.getText(0));
			item.setText(1, i.getUrl());
			item.setText(2, convertLocaldateToString(i.getStart_date()));
			item.setText(3, convertLocaldateToString(i.getEnd_date()));
			item.setText(4, Double.toString(i.getEntry_fee()));
			item.setText(5, i.getCity());
			item.setText(6, i.getAddress());
		}

		for (int loopIndex = 0; loopIndex < titles.length; loopIndex++) {
			table.getColumn(loopIndex).pack();
		}

	}

	public static void main(String[] args) throws SQLException {

		System.setProperty("SWT_GTK3", "0");
		Display display = new Display();
		Shell shell = new Shell(display);

		// shell.setSize(300, 300);
		shell.setText("Conference List");

		GridLayout gridLayout = new GridLayout();

		shell.setLayout(gridLayout);

		shell.setLocation(400, 200);

		shell.layout(true, true);

		final Point newSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

		shell.setSize(new Point(716, 381));

		/*
		 * Initialize Group conferencesInfos which will include : -The Grid data
		 * which will display conferences
		 *
		 * -The fields that will allow the user to add a new conference in the
		 * database
		 */
		Group grp_conferencesInfos = new Group(shell, SWT.NONE);
		GridData gd_conferencesInfos = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_conferencesInfos.heightHint = 215;
		gd_conferencesInfos.widthHint = 677;
		grp_conferencesInfos.setLayoutData(gd_conferencesInfos);
		grp_conferencesInfos.setText("Conferences");

		Table table = new Table(grp_conferencesInfos, SWT.V_SCROLL);
		table.setBounds(165, 28, 502, 134);
		table.setHeaderVisible(true);
		fillConferenceTable(table);

		Button btn_addNewConf = new Button(grp_conferencesInfos, SWT.NONE);
		btn_addNewConf.setBounds(10, 180, 149, 25);
		btn_addNewConf.setText("Create conference");
		
		Label lblCity= new Label(grp_conferencesInfos, SWT.NONE);
		lblCity.setAlignment(SWT.RIGHT);
		lblCity.setBounds(25, 38, 50, 15);
		lblCity.setText("City");
		Text txt_city = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_city.setBounds(81, 35, 78, 21);

		Label lblTitle = new Label(grp_conferencesInfos, SWT.NONE);
		lblTitle.setAlignment(SWT.RIGHT);
		lblTitle.setBounds(25, 38, 50, 15);
		lblTitle.setText("Title");
		Text txt_title = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_title.setBounds(81, 35, 78, 21);

		Label lblUrl = new Label(grp_conferencesInfos, SWT.NONE);
		lblUrl.setAlignment(SWT.RIGHT);
		lblUrl.setText("URL");
		lblUrl.setBounds(25, 66, 50, 15);
		Text txt_url = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_url.setBounds(81, 63, 78, 21);

		Label lblStartDate = new Label(grp_conferencesInfos, SWT.NONE);
		lblStartDate.setAlignment(SWT.RIGHT);
		lblStartDate.setText("Start Date");
		lblStartDate.setBounds(10, 93, 65, 15);
		Text txt_startDate = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_startDate.setBounds(81, 90, 78, 21);
		
		Label lblEndDate = new Label(grp_conferencesInfos, SWT.NONE);
		lblEndDate.setAlignment(SWT.RIGHT);
		lblEndDate.setText("End Date");
		lblEndDate.setBounds(10, 120, 65, 15);
		Text txt_endDate = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_endDate.setBounds(81, 117, 78, 21);
		
		Label lblFee = new Label(grp_conferencesInfos, SWT.NONE);
		lblFee.setText("Fee");
		lblFee.setBounds(42, 147, 33, 15);
		Text txt_fee = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_fee.setBounds(81, 144, 78, 21);

		// add news fields 
		
		/*Label lblCity= new Label(grp_conferencesInfos, SWT.NONE);
		lblCity.setAlignment(SWT.RIGHT);
		lblCity.setBounds(25, 38, 50, 15);
		lblCity.setText("City");
		Text txt_city = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_city.setBounds(81, 35, 78, 21);*/
		
		Label lbladdress= new Label(grp_conferencesInfos, SWT.NONE);
		lbladdress.setAlignment(SWT.RIGHT);
		lbladdress.setBounds(25, 38, 50, 15);
		lbladdress.setText("Address");
		Text txt_address = new Text(grp_conferencesInfos, SWT.BORDER);
		txt_address.setBounds(81, 35, 78, 21);
		
		
		

		// Create new Conference object, assign it values from
		// selection, then pass it function to export to desktop
		// public Conference(int id, String title, String url, LocalDate
		// start_date, LocalDate end_date, double entry_fee)
		Button btnExportEvent = new Button(grp_conferencesInfos, SWT.NONE);
		btnExportEvent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");

				TableItem[] ti = table.getSelection();

				if (ti.length == 0) {
					// Put somewhere that an event should be selected
				} else {
					Conference conf = new Conference(ti[0].getText(0), ti[0].getText(1),
							LocalDate.parse(ti[0].getText(2), formatter), LocalDate.parse(ti[0].getText(3), formatter),
							Double.parseDouble(ti[0].getText(4)), ti[0].getText(5), ti[0].getText(6));
					try {
						FileDialog dialog = new FileDialog(shell, SWT.SAVE);
						dialog.setFilterNames(new String[] { "Calendar Files", "All Files (*.*)" });
						dialog.setFilterExtensions(new String[] { "*.ics", "*.*" }); // Windows

						switch (Util.getOS()) {
						case WINDOWS:
							dialog.setFilterPath("c:\\");
							break;
						case LINUX:
							dialog.setFilterPath("/");
							break;
						case MAC:
							dialog.setFilterPath("/");
							break;
						case SOLARIS:
							dialog.setFilterPath("/");
							break;
						default:
							dialog.setFilterPath("/");
						}

						dialog.setFileName("conference.ics");
						conf.generateCalendarFile(dialog.open());
					} catch (IOException | ValidationException | ParserException e2) {
						e2.printStackTrace();
					}
				}

			}
		});
		btnExportEvent.setBounds(165, 178, 104, 28);
		btnExportEvent.setText("Export Event");

		/*
		 * Behavior of a click on the add new conference button
		 */
		btn_addNewConf.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Appui sur le bouton");
				if (!txt_title.getText().isEmpty() && !txt_url.getText().isEmpty() && !txt_startDate.getText().isEmpty()
						&& !txt_startDate.getText().isEmpty() && !txt_endDate.getText().isEmpty()
						&& !txt_fee.getText().isEmpty()) {

					try {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						LocalDate startDate = LocalDate.parse(txt_startDate.getText(), formatter);
						LocalDate endDate = LocalDate.parse(txt_endDate.getText(), formatter);
						Conference conf = new Conference(txt_title.getText(), txt_url.getText(), startDate, endDate,
								Double.parseDouble(txt_fee.getText()),txt_city.getText(), txt_address.getText() );
						ConferenceDatabase.insertInDatabase(conf);
						/*
						 * Reload the conference table
						 */
						table.removeAll();
						fillConferenceTable(table);

					} catch (DateTimeParseException e1) {
						MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
						mb.setText("Date format error");
						mb.setMessage("Please fill the dates in the following format : dd/MM/YYYY");
						mb.open();
					} catch (SQLException e2) {
						throw new IllegalStateException(e2);
					}
				} else {
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					mb.setText("Information missing");
					mb.setMessage("Please fill in the information regarding the conference");
					mb.open();
				}

			}
		});

		/*
		 * This group will help the user to see his itinerary into the browser
		 */

		Group grp_map = new Group(shell, SWT.NONE);
		grp_map.setText("Visualize your travel");
		GridData gd_map = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1);
		gd_map.heightHint = 85;
		gd_map.widthHint = 679;
		grp_map.setLayoutData(gd_map);
		Text departure = new Text(grp_map, SWT.BORDER);
		departure.setLocation(101, 22);
		departure.setSize(196, 21);
		Text arrival = new Text(grp_map, SWT.BORDER);
		arrival.setLocation(101, 51);
		arrival.setSize(196, 21);

		Button btnItinerary = new Button(grp_map, SWT.NONE);
		btnItinerary.setText("Validate");
		btnItinerary.setBounds(303, 49, 68, 25);

		Label lblDeparture = new Label(grp_map, SWT.NONE);
		lblDeparture.setAlignment(SWT.RIGHT);
		lblDeparture.setBounds(10, 25, 73, 15);
		lblDeparture.setText("Departure");

		Label lblArrival = new Label(grp_map, SWT.NONE);
		lblArrival.setAlignment(SWT.RIGHT);
		lblArrival.setBounds(10, 54, 73, 15);
		lblArrival.setText("Arrival");

		/*
		 * Behavior of the btnItinerary : Takes the addresses entered in
		 * departure and arrival Texts and call the ItineraryMap class to open
		 * the itinerary into the browser
		 */
		btnItinerary.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("Appui sur le bouton");
				if (!departure.getText().isEmpty() && !arrival.getText().isEmpty()) {
					try {
						AddressInfos dep = new AddressInfos(departure.getText());
						AddressInfos arr = new AddressInfos(arrival.getText());
						// ItineraryMap itinerary = new
						// ItineraryMap(dep.getLongitude(),
						// dep.getLatitude(),arr.getLongitude(),
						// arr.getLatitude());

						GoogleItineraryMap itinerary = new GoogleItineraryMap(departure.getText(), arrival.getText());
						String url = itinerary.setMapUrl();
						System.out.println(url);
						itinerary.openMapUrl(url);

					} catch (IllegalArgumentException e) {
						LOGGER.error("Error : ", e);
					}
				} else {
					MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
					mb.setText("Infromation missing");
					mb.setMessage("Please fill in the departure and arrival fields");
					mb.open();
				}

			}
		});
		arrival.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				System.out.println("nouvelle valeur = " + ((Text) e.widget).getText());
			}
		});
		departure.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				System.out.println("nouvelle valeur = " + ((Text) e.widget).getText());
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}