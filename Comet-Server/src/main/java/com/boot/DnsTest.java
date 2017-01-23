package com.boot;

import org.xbill.DNS.Address;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DnsTest {
    public static void main(String[] args) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            displayStuff("local host", inetAddress);
            System.out.print("--------------------------");
            inetAddress = Address.getByName("www.google.com");
            displayStuff("www.google.com", inetAddress);
            System.out.print("--------------------------");
            InetAddress[] inetAddressArray = InetAddress.getAllByName("www.google.com");
            for (int i = 0; i < inetAddressArray.length; i++) {
                displayStuff("www.google.com #" + (i + 1), inetAddressArray[i]);
            }

            System.out.print("--------------------------");
            inetAddress = Address.getByName("master.cometproject.com");
            displayStuff("master.cometproject.com", inetAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void displayStuff(String whichHost, InetAddress inetAddress) {
        System.out.println("--------------------------");
        System.out.println("Which Host:" + whichHost);
        System.out.println("Canonical Host Name:" + inetAddress.getCanonicalHostName());
        System.out.println("Host Name:" + inetAddress.getHostName());
        System.out.println("Host Address:" + inetAddress.getHostAddress());
    }
}
