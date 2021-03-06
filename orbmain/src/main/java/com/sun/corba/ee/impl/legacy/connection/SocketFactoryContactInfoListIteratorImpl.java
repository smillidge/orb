/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Eclipse Distribution License
 * v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause OR GPL-2.0 WITH
 * Classpath-exception-2.0
 */

package com.sun.corba.ee.impl.legacy.connection;

import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;

import com.sun.corba.ee.spi.legacy.connection.GetEndPointInfoAgainException;
import com.sun.corba.ee.spi.orb.ORB;
import com.sun.corba.ee.spi.transport.ContactInfo;
import com.sun.corba.ee.spi.transport.ContactInfoList;
import com.sun.corba.ee.spi.transport.SocketInfo;

import com.sun.corba.ee.spi.logging.ORBUtilSystemException;
import com.sun.corba.ee.impl.transport.ContactInfoListIteratorImpl;
import com.sun.corba.ee.impl.transport.SharedCDRContactInfoImpl;
import com.sun.corba.ee.spi.trace.IsLocal;

@IsLocal
public class SocketFactoryContactInfoListIteratorImpl
    extends ContactInfoListIteratorImpl
{
    private SocketInfo socketInfoCookie;

    public SocketFactoryContactInfoListIteratorImpl(
        ORB orb,
        ContactInfoList corbaContactInfoList)
    {
        super(orb, corbaContactInfoList, null, null, false);
    }

    ////////////////////////////////////////////////////
    //
    // java.util.Iterator
    //

    @Override
    @IsLocal
    public boolean hasNext()
    {
        return true;
    }

    @Override
    @IsLocal
    public ContactInfo next()
    {
        if (contactInfoList.getEffectiveTargetIOR().getProfile().isLocal()){
            return new SharedCDRContactInfoImpl(
                orb, contactInfoList,
                contactInfoList.getEffectiveTargetIOR(),
                orb.getORBData().getGIOPAddressDisposition());
        } else {
            // REVISIT:
            // on comm_failure maybe need to give IOR instead of located.
            return new SocketFactoryContactInfoImpl(
                orb, contactInfoList,
                contactInfoList.getEffectiveTargetIOR(),
                orb.getORBData().getGIOPAddressDisposition(),
                socketInfoCookie);
        }
    }

    @Override
    public boolean reportException(ContactInfo contactInfo,
                                   RuntimeException ex)
    {
        this.failureException = ex;
        if (ex instanceof org.omg.CORBA.COMM_FAILURE) {

            SystemException se = (SystemException) ex;

            if (se.minor == ORBUtilSystemException.CONNECTION_REBIND)
            {
                return true;
            } else {
                if (ex.getCause() instanceof GetEndPointInfoAgainException) {
                    socketInfoCookie = 
                        ((GetEndPointInfoAgainException) ex.getCause())
                        .getEndPointInfo();
                    return true;
                }

                if (se.completed == CompletionStatus.COMPLETED_NO) {
                    if (contactInfoList.getEffectiveTargetIOR() !=
                        contactInfoList.getTargetIOR()) 
                    {
                        // retry from root ior
                        contactInfoList.setEffectiveTargetIOR(
                            contactInfoList.getTargetIOR());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

// End of file.
