/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import java.nio.charset.StandardCharsets;

import org.eclipse.swt.internal.cocoa.NSData;
import org.eclipse.swt.internal.cocoa.NSString;

/**
 * As of ND2 Sprint 22, the {@link NDTextTransfer} is only used as a marker 
 * allowing to distinguish the external incoming TextTransfer against the TextTransfer put 
 * into the clipboard by ND for pasting outside of ND
 * <p/>
 * To distinguish between the cases, whenever CopyRichTextCommand puts into the clipboard the triplet of
 * <ul>
 * <li>normal TextTransfer, for external use</li>
 * <li>NDTextTransfer, to mark that more structured RichModelClipboardData is available in the InternalClipboard</li>
 * <li>and finally, the RichModelClipbardData is put into the separate not dnd-related InternalClipboard for internal use</li>
 * </ul> 
 * <p/>
 * This way, the internal pasting code may check whether DND clipboard has a NDTextTransfer, and if yes, ignore it and use the data from InternalClipboard.
 * <p/>
 * FIXME: [MG] It would be better to wrap the internal clipboard into the DND local only transfer (see e.g EMF LocalTransfer, 
 * http://download.eclipse.org/modeling/emf/emf/javadoc/2.6.0/org/eclipse/emf/edit/ui/dnd/LocalTransfer.html).
 * @author golubev
 */
public class NDTextTransfer extends TextTransfer {

    public static final String ND_TEXT = "NDTextTransfer";

    private static NDTextTransfer _instance = new NDTextTransfer();

    private NDTextTransfer() {
        super(ND_TEXT, registerType(ND_TEXT));
    }

    /**
     * Returns the singleton instance of the TextTransfer class.
     *
     * @return the singleton instance of the TextTransfer class
     */
    public static NDTextTransfer getInstance() {
        return _instance;
    }

    @Override
    public void javaToNative(Object object, TransferData transferData) {
        // can't use super directly, since TextTransfer is one of a few transfers allowed to produce NSString
        // see ND-1836
        // super.javaToNative(object, transferData);

        if (!checkText(object) || !isSupportedType(transferData)) {
            DND.error(DND.ERROR_INVALID_DATA);
        }

        byte[] bytes = ((String) object).getBytes(StandardCharsets.UTF_8);
        transferData.data = NSData.dataWithBytes(bytes, bytes.length);
    }

    @Override
    public Object nativeToJava(TransferData transferData) {
        if (!isSupportedType(transferData) || transferData.data == null)
            return null;
        if (transferData.data instanceof NSString) {
            // should not happen, we have overridden javaToNative, but just in case
            return ((NSString) transferData.data).getString();
        }
        NSData data = (NSData) transferData.data;

        int length = (int) data.length();
        byte[] bytes = new byte[length];
        data.getBytes(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }
}
