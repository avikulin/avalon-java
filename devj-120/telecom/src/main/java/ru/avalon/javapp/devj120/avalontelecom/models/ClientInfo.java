package ru.avalon.javapp.devj120.avalontelecom.models;

import ru.avalon.javapp.devj120.avalontelecom.enums.ClientType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Keeps information about a company client.
 */
public class ClientInfo implements Serializable {
    public static final int MIN_YEAR_ALLOWED = 1920;

    private ClientType type;
    /**
     * Phone number.
     */
    private final PhoneNumber phoneNumber;
    /**
     * Registration date. The field is filled by {@linkplain ClientInfo#ClientInfo constructor}.
     */
    private final LocalDate regDate;
    /**
     * Client name.
     */
    private String name;
    /**
     * Client address.
     */
    private String address;

    /**
     * Individual person's birth date
     */
    private LocalDate birthDate;

    /**
     * Company's manager's name
     */
    private String managerName;

    /**
     * Company's contact person's name
     */
    private String contactPersonName;

    /**
     * Private constructor
     * Initializes general instance attributes with values from corresponding constructor parameters.
     * Sets client registration date to current (today) date.
     *
     * @throws IllegalArgumentException If either {@code phoneNumber}, or {@code name},
     *                                  or {@code address} is {@code null}.
     */
    private ClientInfo(PhoneNumber phoneNumber, String name, String address) throws IllegalArgumentException,
            IllegalStateException {
        if (phoneNumber == null)
            throw new IllegalArgumentException("phone number can't be null.");

        this.phoneNumber = phoneNumber;
        this.regDate = LocalDate.now();
        setName(name);
        setAddress(address);
        setBirthDate(birthDate);
    }

    /**
     * Constructor for individual person
     */
    public ClientInfo(PhoneNumber phoneNumber, String name, String address, LocalDate birthDate)
            throws IllegalArgumentException, IllegalStateException {
        this(phoneNumber, name, address);
        setType(ClientType.PERSON);
        setBirthDate(birthDate);
    }

    /**
     * Constructor for companies
     */
    public ClientInfo(PhoneNumber phoneNumber, String name, String address,
                      String managerName, String contactPersonName) {
        this(phoneNumber, name, address);
        setType(ClientType.COMPANY);
        setManagerName(managerName);
        setContactPersonName(contactPersonName);
    }

    /**
     * Returns client's type
     *
     * @return {@code ClientType}
     */
    public ClientType getType() {
        return type;
    }

    /**
     * Sets client type
     * @param type
     */
    public void setType(ClientType type){
        if (type == null){
            throw new IllegalArgumentException("Type reference param must be not null");
        }

        this.type = type;
        if (type == ClientType.COMPANY){
            this.birthDate = null;
        }
        if (type == ClientType.PERSON){
            this.managerName = null;
            this.contactPersonName = null;
        }
    }

    /**
     * Returns client phone number.
     * This attribute has no setter.
     */
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns registration date.
     * This attribute has no setter.
     */
    public LocalDate getRegDate() {
        return regDate;
    }

    /**
     * Returns client name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets client name.
     *
     * @throws IllegalArgumentException if {@code address} is {@code null}.
     */
    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException("name can't be null.");
        this.name = name;
    }

    /**
     * Returns client address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets client address.
     *
     * @throws IllegalArgumentException if {@code address} is {@code null}.
     */
    public void setAddress(String address) {
        if (address == null)
            throw new IllegalArgumentException("address can't be null.");
        this.address = address;
    }

    /**
     * Returns birth date of an individual person or {@code null} for company
     *
     * @return {@code ClientType} or {@code null}
     */
    public LocalDate getBirthDate() {
        return (type == ClientType.PERSON) ? birthDate : null;
    }

    /**
     * Returns person's age in years or {@code null} for a company
     *
     * @return {@code Integer} or {@code null}
     */
    public Integer getAge() {
        return (type == ClientType.PERSON) ? (int) ChronoUnit.YEARS.between(birthDate, LocalDate.now()) : null;
    }

    /**
     * Sets birth date of an individual person
     *
     * @param birthDate
     * @throws IllegalStateException if {@code birthDate} is {@code null} or empty
     * @throws IllegalStateException if client is a company
     */
    public void setBirthDate(LocalDate birthDate) throws IllegalArgumentException, IllegalStateException {
        if (type == ClientType.PERSON &&
                (birthDate == null ||
                        birthDate.compareTo(LocalDate.of(MIN_YEAR_ALLOWED, 1, 1)) < 0))
            throw new IllegalArgumentException("Birth date of the person can't be null or less than 1920.");

        if (type == ClientType.COMPANY)
            throw new IllegalStateException("Birth date can't be set for company.");

        this.birthDate = birthDate;
    }

    /**
     * Returns manager's name of a company or {@code null} for an individual person
     *
     * @return {@code String} or {@code null}
     */
    public String getManagerName() {
        return (type == ClientType.COMPANY) ? managerName : null;
    }

    /**
     * Sets manager's name of a company
     *
     * @param managerName
     * @throws IllegalStateException if {@code managerName} is {@code null} or empty
     * @throws IllegalStateException if client is an individual person
     */
    public void setManagerName(String managerName) {
        if (type == ClientType.COMPANY && (managerName == null || managerName.isEmpty()))
            throw new IllegalArgumentException("Manager's name of the company can't be null.");
        if (type == ClientType.PERSON)
            throw new IllegalStateException("Manager's name can't be set for an individual person.");

        this.managerName = managerName;
    }

    /**
     * Sets contact person's name of a company
     *
     * @param contactPersonName
     * @throws IllegalStateException if {@code contactPersonName} is {@code null} or empty
     * @throws IllegalStateException if client is an individual person
     */
    public void setContactPersonName(String contactPersonName) {
        if (type == ClientType.COMPANY && (contactPersonName == null || contactPersonName.isEmpty()))
            throw new IllegalArgumentException("Contact person's name of the company can't be null.");
        if (type == ClientType.PERSON)
            throw new IllegalStateException("Contact person's name can't be set for an individual person.");

        this.contactPersonName = contactPersonName;
    }

    /**
     * Returns contact person's name of a company or {@code null} for an individual person
     *
     * @return {@code String} or {@code null}
     */
    public String getContactPersonName() {
        return (type == ClientType.COMPANY) ? contactPersonName : null;
    }


}
