package ru.avalon.javapp.devj120.avalontelecom.lists;

import ru.avalon.javapp.devj120.avalontelecom.models.ClientInfo;
import ru.avalon.javapp.devj120.avalontelecom.models.PhoneNumber;

import javax.management.OperationsException;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages list of clients.
 */
public class ClientList {
    private static final String DATA_FILE_NAME = "clients.dat";
    /**
     * The only instance of this class.
     *
     * @see #getInstance
     */
    private static ClientList instance;

    /**
     * Keeps the list of of clients. Clients are stored in the same order,
     * which they have been registered in.
     */
    private List<ClientInfo> clients;

    /**
     * List of client phone numbers. The list is used by
     * {@link #addPerson(PhoneNumber, String, String, LocalDate)} and
     * {@link #addCompany(PhoneNumber, String, String, String, String)}method to check,
     * that specified phone number is not used.
     */
    private Set<PhoneNumber> numbers;

    /**
     * Prevents instance creation out of the class.
     */
    private ClientList() throws IllegalStateException {
        File f = new File(DATA_FILE_NAME);
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                clients = (List<ClientInfo>) ois.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                throw new IllegalStateException("Can't read data from file");
            }
            numbers = new HashSet<>();
            for (ClientInfo info : clients) {
                numbers.add(info.getPhoneNumber());
            }
        } else {
            clients = new ArrayList<>();
            numbers = new HashSet<>();
        }
    }

    /**
     * Adds an individual person  with specified attributes. Creates instance of {@code ClientInfo}
     * (see {@link ClientInfo#ClientInfo(PhoneNumber, String, String, LocalDate)}) while adding new client.
     *
     * @param number  client phone number
     * @param name    client name
     * @param address client address
     * @throws IllegalArgumentException If some client with specified phone number
     *                                  has already been registered.
     */
    public void addPerson(PhoneNumber number, String name, String address, LocalDate birthDate) throws
            IllegalArgumentException, IllegalStateException {
        if (numbers.contains(number))
            throw new IllegalArgumentException("Such a number has already been registered earlier.");
        clients.add(new ClientInfo(number, name, address, birthDate));
        numbers.add(number);
    }

    /**
     * Adds a company with specified attributes. Creates instance of {@code ClientInfo}
     * (see {@link ClientInfo#ClientInfo(PhoneNumber, String, String, String, String)}) while adding new client.
     *
     * @param number  client phone number
     * @param name    client name
     * @param address client address
     * @throws IllegalArgumentException If some client with specified phone number
     *                                  has already been registered.
     */
    public void addCompany(PhoneNumber number, String name, String address,
                           String managerName, String contactPersonName) throws
            IllegalArgumentException,
            IllegalStateException {
        if (numbers.contains(number))
            throw new IllegalArgumentException("Such a number has already been registered earlier.");
        clients.add(new ClientInfo(number, name, address, managerName, contactPersonName));
        numbers.add(number);
    }


    /**
     * Removes client with the specified index.
     *
     * @param index index of a client to be removed
     * @throws IndexOutOfBoundsException If the index is out of range (index < 0 || index >= {@link #getClientsCount}).
     */
    public void remove(int index) {
        ClientInfo clientInfo = clients.get(index);
        numbers.remove(clientInfo.getPhoneNumber());
        clients.remove(index);
    }

    /**
     * Returns amount of clients, kept by the list.
     *
     * @return Number of clients, kept by the list.
     */
    public int getClientsCount() {
        return clients.size();
    }

    /**
     * Returns information about a client with specified index.
     *
     * @param index client index, which data is retrieved
     * @return {@code ClientInfo}
     * @throws IndexOutOfBoundsException If the index is out of range (index < 0 || index >= {@link #getClientsCount}).
     */
    public ClientInfo getClientInfo(int index) {
        return clients.get(index);
    }

    /**
     * Returns the only instance of this class.
     */
    public static ClientList getInstance() {
        if (instance == null) {
            instance = new ClientList();
        }
        return instance;
    }

    public void saveToFile() throws OperationsException {
        if (clients.isEmpty()){
            return;
        }

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE_NAME))) {
            oos.writeObject(clients);
        } catch (IOException e) {
            throw new OperationsException("Can't save data to file");
        }
    }
}
