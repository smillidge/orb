/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates.
 * Copyright (c) 1998-1999 IBM Corp. All rights reserved.
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

package javax.rmi.CORBA.serialization;

import java.util.*;
import java.io.*;
import org.omg.CORBA.TypeCode;

public abstract class IDLValueHelper
{
    public static void insert (org.omg.CORBA.Any a, IDLValue that)
    {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
        a.type (type ());
        write (out, that);
        a.read_value (out.create_input_stream (), type ());
    }

    public static IDLValue extract (org.omg.CORBA.Any a)
    {
        return read (a.create_input_stream ());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;
    private static boolean __active = false;
    synchronized public static org.omg.CORBA.TypeCode type ()
    {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( id() );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [1];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[0] = new org.omg.CORBA.StructMember (
            "foo",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (id(), "IDLValue", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
    }

    public static String id()
    {
        return "IDL:javax/rmi/CORBA/serialization/IDLValue:1.0";
    }

    public static IDLValue read (org.omg.CORBA.portable.InputStream istream)
    {
        IDLValue value = new IDLValue();
        value.fInt = istream.read_long();
        value.fLong = istream.read_longlong();
        value.fFloat = istream.read_float();
        value.fDouble = istream.read_double();
        value.fString = istream.read_string();
        return value;
    }
        
    public static void write (org.omg.CORBA.portable.OutputStream ostream, IDLValue value)
    {
        ostream.write_long(value.fInt);
        ostream.write_longlong(value.fLong);
        ostream.write_float(value.fFloat);
        ostream.write_double(value.fDouble);
        ostream.write_string(value.fString);
    }
        
}
