#
# Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License v. 2.0 which is available at
# http://www.eclipse.org/legal/epl-2.0, or the Eclipse Distribution License
# v. 1.0 which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the Eclipse
# Public License v. 2.0 are satisfied: GNU General Public License v2.0
# w/Classpath exception which is available at
# https://www.gnu.org/software/classpath/license.html.
#
# SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause OR GPL-2.0 WITH
# Classpath-exception-2.0
#

include Rules.jmk

all: build test1

build: idl src

idl:
        $(IDLJ) -td $(TOOLS_OUTPUTDIR) -fall -pkgPrefix IIOP org.omg -pkgPrefix IOP org.omg IIOP.idl
        $(IDLJ) -td $(TOOLS_OUTPUTDIR) -fall -pkgPrefix IIOP org.omg -pkgPrefix IOP org.omg IOP.idl
        $(IDLJ) -td $(TOOLS_OUTPUTDIR) -fall -pkgPrefix CONV_FRAME org.omg CONV_FRAME.idl
        $(JAVAC) -classpath $(CLASSPATH) -d $(TOOLS_OUTPUTDIR) $(TOOLS_OUTPUTDIR)/org/omg/IOP/*.java $(TOOLS_OUTPUTDIR)/org/omg/IIOP/*.java
        $(JAVAC) -classpath $(CLASSPATH) -d $(TOOLS_OUTPUTDIR) $(TOOLS_OUTPUTDIR)/org/omg/CONV_FRAME/*.java

src:
        $(JAVAC) -d $(TOOLS_OUTPUTDIR) -classpath $(CLASSPATH) IORDecoder.java TextOutputHandler.java IIOPProfileHandler.java Utility.java EncapsHandler.java TagHelperHandler.java CodeBaseHandler.java DecodingException.java InitializationException.java TaggedMapFileReader.java

test1:
        $(JAVA) $(ORB_PROPS) -classpath $(CLASSPATH) tools.ior.IORDecoder IOR:0000000000000026524d493a5044617461312e4461746131486f6d653a3030303030303030303030303030303000000000000001000000000000018c000102000000000f3133302e3231342e36302e3133320000041a00000000003eafabcb0000000022000000640000000100000000000000010000000e50657273697374656e74504f410000000000000d0000415514cc000000000001ff0300000000000500000001000000200000000000010001000000020501000100010020000101090000000100010100000000210000006800000000000000010040000000000024000000100000000000000424000000660000000000400040000000080606678102010101000000170401000806066781020101010000000764656661756c74000400000000000000000000010000000806066781020101010000001900000070000000000000006866696c653a2f453a2f6a3273646b6565312e332f7265706f7369746f72792f616a6f7368696e742f6170706c69636174696f6e732f41707031436c69656e742e6a617220687474703a2f2f616a6f7368696e743a393139312f41707031436c69656e742e6a6172000000001f0000000400000003000000200000000400000001

clean:
        $(RM) -r $(TOOLS_OUTPUTDIR)
