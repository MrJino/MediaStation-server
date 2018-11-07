package noh.jinil.boot.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class VideoMetadataReader {

	enum META_TYPE {
		NONE,
		INPUT,
		VIDEO,
	}
	
	enum VIDEO_INFO {
		CODEC,
		COLOR_FORMAT,
		RESOLUTION,
		BIT_PER_SEC,
		FRAME_PER_SEC,	
		ETC
	}
	
	/**
	 * parse meta data from video file
	 * 
	 * ->  Input #0, mov,mp4,m4a,3gp,3g2,mj2, from 'c:\NOH\STS\eystation\data\photo\temp\IMG_5359.MOV':
     * ->  Metadata:
     * ->    major_brand     : qt  
     * ->    minor_version   : 0
     * ->    compatible_brands: qt  
     * ->    creation_time   : 2018-01-13T01:53:29.000000Z
     * ->    com.apple.quicktime.make: Apple
     * ->    com.apple.quicktime.model: iPhone 6s
     * ->    com.apple.quicktime.software: 11.2.1
     * ->    com.apple.quicktime.creationdate: 2018-01-13T10:53:28+0900
     * ->  Duration: 00:00:04.30, start: 0.000000, bitrate: 13422 kb/s
     * ->    Stream #0:0(und): Video: h264 (High) (avc1 / 0x31637661), yuv420p(tv, bt709), 1920x1080, 13311 kb/s, 29.98 fps, 29.97 tbr, 600 tbn, 1200 tbc (default)
     * ->    Metadata:
     * ->      creation_time   : 2018-01-13T01:53:29.000000Z
     * ->      handler_name    : Core Media Data Handler
     * ->      encoder         : H.264
     * ->    Stream #0:1(und): Audio: aac (LC) (mp4a / 0x6134706D), 44100 Hz, mono, fltp, 95 kb/s (default)
     * ->    Metadata:
     * ->      creation_time   : 2018-01-13T01:53:29.000000Z
     * ->      handler_name    : Core Media Data Handler
     * ->    Stream #0:2(und): Data: none (mebx / 0x7862656D), 0 kb/s (default)
     * ->    Metadata:
     * ->      creation_time   : 2018-01-13T01:53:29.000000Z
     * ->      handler_name    : Core Media Data Handler
     * ->    Stream #0:3(und): Data: none (mebx / 0x7862656D), 0 kb/s (default)
     * ->    Metadata:
     * ->      creation_time   : 2018-01-13T01:53:29.000000Z
     * ->      handler_name    : Core Media Data Handler
     * ->At least one output file must be specified
     * 
	 * @param metadataStr meta data
	 * @return Metadata based on key,value pairs
	 */
	public static Metadata parseMetadata(String metadataStr) {

		Metadata metaData = new Metadata();
		
		if (metadataStr == null)
			return metaData;
		
		META_TYPE type = META_TYPE.NONE;
		String str;
		BufferedReader reader = new BufferedReader(new StringReader(metadataStr));
		try {
			while ((str = reader.readLine()) != null) {
				//logger.debug("->"+str);
				String convertStr = str.trim().toLowerCase();
				
				//  meta type check
				if (convertStr.startsWith("input #")) {
					type = META_TYPE.INPUT;
				}
				else if (convertStr.startsWith("stream #")) {
					if(convertStr.contains("video:")) {
						type = META_TYPE.VIDEO;						
					}
				}
				
				// parsing according to meta data type
				switch (type) 
				{
				case INPUT:
					parseMainMeta(str, metaData);
					break;
				case VIDEO:
					parseVideoMeta(str, metaData);
					break;
				default:
					// do nothing
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return metaData;
	}
	
	private static void parseMainMeta(String str, Metadata metadata) {
		String convertStr = str.trim().toLowerCase();
		
		// creation_time   : 2018-01-13T01:53:29.000000Z
		if(convertStr.startsWith("creation_time")) {
			String createTime = convertStr.substring(convertStr.indexOf(':')+1).trim();
			metadata.setTag("creation_time", createTime);
			
			String data[];
			if (createTime.contains("t"))
				data = createTime.split("t");  	// 2018-01-13T01:53:29.000000Z
			else
				data = createTime.split(" ");	// 2018-01-13 01:53:29
						
			if (data.length >= 2) {
				metadata.setTag("date", data[0]);
				if(data[1].contains("."))
					metadata.setTag("time", data[1].substring(0, data[1].indexOf('.')));
				else
					metadata.setTag("time", data[1]);
			}
		}
		// com.apple.quicktime.make: Apple
		else if(convertStr.startsWith("com.apple.quicktime.make")) {
			String make = convertStr.substring(convertStr.indexOf(':')+1).trim();
			metadata.setTag("make", make);
		}
		// com.apple.quicktime.model: iPhone 6s
		else if(convertStr.startsWith("com.apple.quicktime.model")) {
			String model = convertStr.substring(convertStr.indexOf(':')+1).trim();
			metadata.setTag("model", model);
		}
	}
	
	private static void parseVideoMeta(String str, Metadata metadata) {
		String convertStr = str.trim().toLowerCase();
		
		if (convertStr.startsWith("stream #") && convertStr.contains("video:")) {
			parseVideoStream(str.trim().substring(convertStr.indexOf("video:")+"video:".length()), metadata);
		}
		else if (convertStr.startsWith("rotate")) {
			String rotate = convertStr.substring(convertStr.indexOf(":")+1).trim();
			metadata.setTag("rotate", rotate);
		}
	}
	
	private static void parseVideoStream(String str, Metadata metadata) {
		String convertStr = str.trim().toLowerCase();
		
		BufferedReader reader = new BufferedReader(new StringReader(convertStr));
		StringBuffer outBuf = new StringBuffer();
		
		try {
			VIDEO_INFO enumVideoInfo = VIDEO_INFO.CODEC;
			int ignoreSpace = 0;
			int b;
			
			while((b = reader.read()) >= 0) {
				char c = (char)b;
				if(c == '(' || c == '[' || c == '{') {
					ignoreSpace++;
					outBuf.append(c);
				}
				else if(c == ')' || c == ']' || c == '}') {
					ignoreSpace--;
					outBuf.append(c);
				}
				else if(c == ',' && ignoreSpace == 0) {
					switch(enumVideoInfo) 
					{
					case CODEC:
						metadata.setTag("codec", outBuf.toString().trim());
						outBuf = new StringBuffer();
						enumVideoInfo = VIDEO_INFO.COLOR_FORMAT;					
						break;	
					case COLOR_FORMAT:
						metadata.setTag("colorformat", outBuf.toString().trim());
						outBuf = new StringBuffer();
						enumVideoInfo = VIDEO_INFO.RESOLUTION;
						break;
					case RESOLUTION:
						metadata.setTag("resolution", outBuf.toString().trim());
						
						Scanner scanner = new Scanner(outBuf.toString().trim());						
						if (scanner.hasNext()) {
							parseResolution(scanner.next(), metadata);
						}
						scanner.close();
						
						outBuf = new StringBuffer();
						enumVideoInfo = VIDEO_INFO.ETC;
						break;
					default:
						break;
					}
				}
				else {
					outBuf.append(c);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param str should be this format like '1280x720'
	 * @param metadata abc
	 */
	private static void parseResolution(String str, Metadata metadata) {
		String convertStr = str.trim().toLowerCase();
		
		String width = convertStr.substring(0, convertStr.indexOf('x'));
		String height = convertStr.substring(convertStr.indexOf('x')+1);
		
		metadata.setTag("width", width);
		metadata.setTag("height", height);
	}	
}
