/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.code;

import java.io.IOException;

public class TranscoderSmart implements Transcoder {

	private final Transcoder oldOne = new TranscoderImpl(new AsciiEncoder(), new CompressionHuffman());
	private final Transcoder zlibBase64 = new TranscoderImpl(new AsciiEncoderBase64(), new CompressionZlib());
	private final Transcoder zlib = new TranscoderImpl(new AsciiEncoder(), new CompressionZlib());
	private final Transcoder brotliBase64 = new TranscoderImpl(new AsciiEncoderBase64(), new CompressionBrotli());

	public String decode(String code) throws IOException {
		if (code.startsWith("0")) {
			return zlibBase64.decode(code.substring(1));
		}
		if (code.startsWith("1")) {
			return brotliBase64.decode(code.substring(1));
		}
		try {
			return zlib.decode(code);
		} catch (Exception ex) {
			return oldOne.decode(code);
		}
		// return zlib.decode(code);
	}

	public String encode(String text) throws IOException {
		return zlib.encode(text);
	}
}
