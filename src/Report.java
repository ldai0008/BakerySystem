import java.time.LocalDate;
/**
 * @version: V1.0
 * @author: Team_04
 * @className: Report
 * @description: Entity Class for report
 * @data: 2020-11-1 9:00
 **/
public class Report {
	// The date of report for Report
	// The name of report for Report
	// The type of report for Report
	// The store for Report
	private LocalDate dateOfReport;
	private String nameOfReport;
	private String typeOfReport;
	private Store store;


	/**
	 * Constructor for objects of class Report
	 * have a parameterised constructor
	 */
	public Report(LocalDate dateOfReport, String nameOfReport, String typeOfReport, Store store) {
		super();
		this.dateOfReport = dateOfReport;
		this.nameOfReport = nameOfReport;
		this.typeOfReport = typeOfReport;
		this.store = store;
	}

	/**
	 * Get method
	 * @return the dateOfReport
	 */
	public LocalDate getDateOfReport() {
		return dateOfReport;
	}

	/**
	 * Mutator method
	 * @param   dateOfReport   the new date of report in the Report
	 */
	public void setDateOfReport(LocalDate dateOfReport) {
		this.dateOfReport = dateOfReport;
	}

	/**
	 * Get method
	 * @return the nameOfReport
	 */
	public String getNameOfReport() {
		return nameOfReport;
	}

	/**
	 * Mutator method
	 * @param   nameOfReport   the new nameOfReport in the Report
	 */
	public void setNameOfReport(String nameOfReport) {
		this.nameOfReport = nameOfReport;
	}

	/**
	 * Get method
	 * @return the typeOfReport
	 */
	public String getTypeOfReport() {
		return typeOfReport;
	}

	/**
	 * Mutator method
	 * @param   typeOfReport   the new type of report in the Report
	 */
	public void setTypeOfReport(String typeOfReport) {
		this.typeOfReport = typeOfReport;
	}

	/**
	 * Get method
	 * @return the store
	 */
	public Store getStore() {
		return store;
	}

	/**
	 * Mutator method
	 * @param   store   the new store in the Report
	 */
	public void setStore(Store store) {
		this.store = store;
	}

}
