/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Outhink - support for typeFileURL
 *******************************************************************************/
package org.eclipse.swt.dnd;

/**
 * The class <code>ImageTransfer</code> provides a platform specific mechanism
 * for converting an Image represented as a java <code>ImageData</code> to a
 * platform specific representation of the data and vice versa.
 * 
 * <p>
 * An example of a java <code>ImageData</code> is shown below:
 * </p>
 * 
 * <code><pre>
 *     Image image = new Image(display, "C:\temp\img1.gif");
 *	   ImageData imgData = image.getImageData();
 * </code>
 * </pre>
 *
 * @see Transfer
 * 
 * @since 3.4
 */
public class NDImageTransfer extends ImageTransfer {

    public static final String ND_IMAGE = "NDImageTransfer";

    private static NDImageTransfer _instance = new NDImageTransfer();

    private NDImageTransfer() {
        super(ND_IMAGE, registerType(ND_IMAGE));
    }

    /**
     * Returns the singleton instance of the ImageTransfer class.
     *
     * @return the singleton instance of the ImageTransfer class
     */
    public static NDImageTransfer getInstance() {
        return _instance;
    }
}
