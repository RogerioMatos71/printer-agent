package com.printeragent.snmp;

import com.printeragent.domain.Printer;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;


public class SnmpClient implements SnmpReader{


    private static final int DEFAULT_TIMEOUT = 1500;
    private static final int DEFAULT_RETRIES = 2;

    @Override 
    public SnmpResult readOid(
            Printer printer,
            String oid) {

        Snmp snmp = null;

        try {

            snmp = new Snmp(
                new DefaultUdpTransportMapping()
            );

            snmp.listen();

            CommunityTarget<UdpAddress> target =
                createTarget(printer);

            PDU pdu = new PDU();

            pdu.add(
                new VariableBinding(
                    new OID(oid)
                )
            );

            pdu.setType(PDU.GET);

            ResponseEvent<UdpAddress> response =
                snmp.get(pdu, target);

            if (response == null ||
                    response.getResponse() == null) {

                return new SnmpResult(
                    oid,
                    null,
                    false,
                    "A impressora não respondeu."
                );
            }

            PDU responsePdu =
                response.getResponse();

            if (responsePdu.getErrorStatus()
                    != PDU.noError) {

                return new SnmpResult(
                    oid,
                    null,
                    false,
                    responsePdu.getErrorStatusText()
                );
            }

            VariableBinding binding =
                responsePdu.get(0);

            if (binding.isException()) {

                return new SnmpResult(
                    binding.getOid().toString(),
                    null,
                    false,
                    binding.getVariable().toString()
                );
            }

            return new SnmpResult(
                binding.getOid().toString(),
                binding.getVariable().toString(),
                true,
                null
            );

        } catch (Exception e) {

            return new SnmpResult(
                oid,
                null,
                false,
                e.getMessage()
            );

        } finally {

            if (snmp != null) {

                try {
                    snmp.close();
                } catch (Exception ignored) {
                    // Nothing else can be done here.
                }
            }
        }
    }

    private CommunityTarget<UdpAddress> createTarget(
            Printer printer) {

        CommunityTarget<UdpAddress> target =
            new CommunityTarget<>();

        target.setCommunity(
            new OctetString(
                printer.getCommunity()
            )
        );

        target.setAddress(
            new UdpAddress(
                printer.getIpAddress()
                    + "/"
                    + printer.getSnmpPort()
            )
        );

        target.setVersion(
            SnmpConstants.version2c
        );

        target.setRetries(DEFAULT_RETRIES);
        target.setTimeout(DEFAULT_TIMEOUT);

        return target;
    }
}


