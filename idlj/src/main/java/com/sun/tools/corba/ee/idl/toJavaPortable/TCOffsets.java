/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 1997-1999 IBM Corp. All rights reserved.
 * Copyright (c) 2019 Payara Services Ltd.
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

package com.sun.tools.corba.ee.idl.toJavaPortable;

// NOTES:

import java.util.Enumeration;
import java.util.Hashtable;

// This class is passed through the JavaGenerator.HelperType methods.
// It is ONLY used when a recursive sequence is detected. ie.
//
//   struct S1
//   {
//     sequence <S1> others;
//   };

/**
 *
 **/
public class TCOffsets
{
  /**
   * Return -1 if the given name is not in the list of types.
   **/
  public int offset (String name)
  {
    Integer value = tcs.get (name);
    return value == null ? -1 : value;
  } // offset

  /**
   *
   **/
  public void set (com.sun.tools.corba.ee.idl.SymtabEntry entry)
  {
    if (entry == null)
      offset += 8;
    else
    {
      tcs.put(entry.fullName(), offset);
      offset += 4;
      String repID = Util.stripLeadingUnderscoresFromID(entry.repositoryID().ID());
      if (entry instanceof com.sun.tools.corba.ee.idl.InterfaceEntry)
        offset += alignStrLen (repID) + alignStrLen (entry.name ());
      else if (entry instanceof com.sun.tools.corba.ee.idl.StructEntry)
        offset += alignStrLen (repID) + alignStrLen (entry.name ()) + 4;
      else if (entry instanceof com.sun.tools.corba.ee.idl.UnionEntry)
        offset += alignStrLen (repID) + alignStrLen (entry.name ()) + 12;
      else if (entry instanceof com.sun.tools.corba.ee.idl.EnumEntry)
      {
        offset += alignStrLen (repID) + alignStrLen (entry.name ()) + 4;
        Enumeration e = ((com.sun.tools.corba.ee.idl.EnumEntry)entry).elements ().elements ();
        while (e.hasMoreElements ())
          offset += alignStrLen ((String)e.nextElement ());
      }
      else if (entry instanceof com.sun.tools.corba.ee.idl.StringEntry)
        offset += 4;
      else if (entry instanceof com.sun.tools.corba.ee.idl.TypedefEntry)
      {
        offset += alignStrLen (repID) + alignStrLen (entry.name ());
        if (!((com.sun.tools.corba.ee.idl.TypedefEntry)entry).arrayInfo ().isEmpty())
          offset += 8;
      }
    }
  } // set

  /**
   * Return the full length of the string type:  4 byte length, x bytes for
   * string + 1 for the null terminator, align it so it ends on a 4-byte
   * boundary.  This method assumes the string starts at a 4-byte boundary
   * since it doesn't do any leading alignment.
   **/
  public int alignStrLen (String string)
  {
    int len = string.length () + 1;
    int align = 4 - (len % 4);
    if (align == 4) align = 0;
    return len + align + 4;
  } // alignStrLen

  /**
   *
   **/
  public void setMember (com.sun.tools.corba.ee.idl.SymtabEntry entry)
  {
    offset += alignStrLen (entry.name ());
    if (!((com.sun.tools.corba.ee.idl.TypedefEntry)entry).arrayInfo ().isEmpty())
      offset += 4;
  } // setMember

  /**
   *
   **/
  public int currentOffset ()
  {
    return offset;
  } // currentOffset

  /**
   *
   **/
  public void bumpCurrentOffset (int value)
  {
    offset += value;
  } // bumpCurrentOffset

  private Hashtable<String, Integer> tcs    = new Hashtable<>();
  private int       offset = 0;
} // class TCOffsets
