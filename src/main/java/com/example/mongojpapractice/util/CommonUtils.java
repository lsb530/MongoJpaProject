package com.example.mongojpapractice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CommonUtils {

    /**
     * 문자열에서 숫자만 남긴다. ex) 전화번호에서 - 부호를 삭제
     *
     * @param str 대상 문자열
     * @return 부호등이 삭제된 문자열
     */
    public static String toOnlyNumStr(String str) {
        if(!ObjectUtils.isEmpty(str)) {
            return str.replaceAll("[^0-9]", "");
        }
        else return null;
    }

    /**
     * String to datetime
     *
     * @param datetimeString 날짜 형식 문자열
     * @return Date 변환 결과 문자열
     */
    public static Date toDateTime(String datetimeString) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd H:m:s");
        Date to = null;
        try {
            to = transFormat.parse(datetimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return to;
    }

    /**
     * 시작일을 기준으로 00시 부터 23:59 분까지의 날짜,시간 범위를 만든다
     *
     * @param startDt String 대상일
     * @return &lt;String,Date> from -> 시작일시 , to -> 종료일시
     */
    public static Map<String, Date> createDateRange(String startDt) {
        Map<String, Date> dateRange = new HashMap<>();

        dateRange.put("from", toDateTime(startDt + " 00:00:00"));
        dateRange.put("to", toDateTime(startDt + " 23:59:59"));

        return dateRange;
    }

    /**
     * 시작일, 종료일을 기준으로 날짜, 시간 범위를 만든다.
     *
     * @param startDt String 시작일
     * @param endDt   종료일
     * @return &lt;String,Date> from -> 시작일시 , to -> 종료일시
     */
    public static Map<String, Date> createDateRange(String startDt, String endDt) {
        Map<String, Date> dateRange = new HashMap<>();

        dateRange.put("from", toDateTime(startDt + " 00:00:00"));
        dateRange.put("to", toDateTime(endDt + " 23:59:59"));

        return dateRange;
    }

    /**
     * 시작일을 기준으로 00시 부터 23:59 분까지의 날짜,시간 범위를 만든다
     *
     * @param startDt LocalDate 대상일
     * @return Map&lt;String,LocalDateTime> from -> 시작일시 , to -> 종료일시
     */
    public static Map<String, LocalDateTime> createDateRange(LocalDate startDt) {
        Map<String, LocalDateTime> dateRange = new HashMap<>();

        var fromLdt = startDt.atStartOfDay();
        var toLdt = startDt.plus(1, ChronoUnit.DAYS).atStartOfDay().minus(1, ChronoUnit.NANOS);

        dateRange.put("from", fromLdt);
        dateRange.put("to", toLdt);

        return dateRange;
    }

    /**
     * 시작일, 종료일을 기준으로 날짜, 시간 범위를 만든다.
     *
     * @param startDt LocalDate 시작일
     * @param endDt   LocalDate 종료일
     * @return Map&lt;String,LocalDateTime> from -> 시작일시 , to -> 종료일시
     */
    public static Map<String, LocalDateTime> createDateRange(LocalDate startDt, LocalDate endDt) {
        Map<String, LocalDateTime> dateRange = new HashMap<>();

        var fromLdt = startDt.atStartOfDay();
        var toLdt = endDt.plus(1, ChronoUnit.DAYS).atStartOfDay().minus(1, ChronoUnit.NANOS);

        dateRange.put("from", fromLdt);
        dateRange.put("to", toLdt);

        return dateRange;
    }

    /**
     * LocalDate 포맷 변경
     *
     * @param targetDate    대상 LocalDate
     * @param patternString 대상 포맷
     * @return String 포맷에 맞춰 변경된 문자열
     */
    public static String replaceDate(LocalDate targetDate, String patternString) {
        return targetDate.format(DateTimeFormatter.ofPattern(patternString));
    }

    /**
     * LocalDate 형식을 YearMonth로 변경
     * @param targetDate 대상 LocalDate
     * @return LocalDate 형식을 YearMonth로 변경된 값
     */
    public static YearMonth localDate2YearMonth(LocalDate targetDate) {
        return YearMonth.from(targetDate);
    }

    /**
     * 이메일 양식 체크
     *
     * @param email 대상 이메일
     * @return boolean True : 올바른 이메일 False : 올바르지 못한 이메일
     */
    public static boolean isValidEmail(String email){
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        if (email == null) {
            return false;
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 비밀번호 양식 체크 - 하나 이상의 숫자, 하나 이상의 소문자, 하나 이상의 대문자,
     *                 하나 이상의 특수문자{@literal(!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~)}, 공백 불가, 8자 이상 20자 이하
     *
     * @param password 대상 비밀번호
     * @return boolean True : 올바른 비밀번호 False : 올바르지 못한 비밀번호
     */
    public static boolean isValidPassword(String password) {
        /*
        하나 이상의 숫자, 하나 이상의 소문자, 하나 이상의 대문자,
        하나 이상의 특수문자(!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~), 공백 불가, 8자 이상 20자 이하
        */
        String regex
            = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.*[^\\\\!\"'#$@%&()~*^+<>,./:|;=\\-?_`\\[\\]_{}A-Za-z0-9])\\S{8,20}";

        if (password == null) {
            return false;
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);

        return m.matches();
    }

    /**
     * 익셉션의 stack trace를 string으로 반환
     *
     * @param e 익셉션 객체
     * @return 익셉션의 stack trace 정보를 담은 string
     */
    public static String getStackTraceString(Throwable e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }

    public static String getAllHeaders(HttpServletRequest httpServletRequest) {
        var rst = new StringBuilder();
        var headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            rst.append(key).append(": ");

            var headerValues = httpServletRequest.getHeaders(key);
            while (headerValues.hasMoreElements()) {
                rst.append(headerValues.nextElement());
                if (headerValues.hasMoreElements()) {
                    rst.append(", ");
                }
            }
            rst.append("\n");
        }

        return rst.toString();
    }

    /**
     * URL에서 파라미터를 파싱한다
     *
     * @param query Url의 파라미터 쿼리 문자열
     * @return {@link Map}{@literal <}{@link String},{@link String }> 파싱 결과가 담긴 Map 객체
     */
    public static Map<String, String> getQueryMap(String query) {
        if (query == null) {
            return null;
        }

        int pos1 = query.indexOf("?");
        if (pos1 >= 0) {
            query = query.substring(pos1 + 1);
        }

        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    /**
     * 외부로 접속할때 사용되는 자기 IP Address를 가져온다
     *
     * @return 외부로 나가는 자기의 IP Address
     */
    public static String getExternalIpAddress() {
        BufferedReader in = null;
        try {
            URL checkIpUrl = new URL("http://checkip.amazonaws.com");
            URLConnection urlConn = checkIpUrl.openConnection();
            urlConn.setConnectTimeout(500);
            urlConn.setReadTimeout(1000);
            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            return in.readLine().strip();
        }
        catch (Exception e) {
            log.error("getExternalIpAddress: {}", e.toString());
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * AWS EC2에서 실행될 경우 InstanceId를 리턴하고
     * 다른 경우에는 호스트네임을 리턴한다
     * 다른 클라우드에서도 실행될 경우 해당 클라우드 관련 코드 추가 구현이 필요하다
     *
     * @return "AWS/InstanceId" or "HOST/Hostname"
     */
    public static String getInstanceId() {
        BufferedReader in = null;
        try {
            URL awsMetadataUrl = new URL("http://169.254.169.254/latest/meta-data/instance-id");
            URLConnection urlConn = awsMetadataUrl.openConnection();
            urlConn.setConnectTimeout(500);
            urlConn.setReadTimeout(1000);
            in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            return "AWS/" + in.readLine().strip();
        }
        catch (NoRouteToHostException|SocketTimeoutException e) {
            try {
                return "HOST/" + InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException unknownHostException) {
                log.error("getEC2InstanceId: {}", unknownHostException.toString());
            }
        }
        catch (Exception e) {
            log.error("getEC2InstanceId: {}", e.toString());
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static BigDecimal getBigDecimal( Object value ) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = BigDecimal.valueOf(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }

    /**
     * List&lt;String&gt;이 null이 아니면 list의 첫번째 값을 리턴하고 null 이면 empty string을 리턴한다
     *
     * @param strList string이 들어있는 리스트
     * @return 리스트의 첫번째 값 또는 empty string을 리턴
     */
    public static String getFirstString(List<String> strList) {
        if (strList != null && strList.size() >= 1) {
            return strList.get(0);
        }
        else {
            return "";
        }
    }

    /**
     * <p>현재 실행중인 클래스패스에서 자원 파일들을 가져온다(Runtime 중[jar, war]에도 적용)</p>
     * <p>spEL(Spring Expression)로검색 가능</p>
     * @param locationPattern 탐색경로(ex: "classpath:\/fonts\/**\/*.otf")
     * @return 리소스 자원들을 가져옴
     */
    public static Resource[] getResources(String locationPattern) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
        return resolver.getResources(locationPattern);
    }

    /**
     * 단일 리소스 자원을 가져온다
     * @param location 리소스 경로 (resource 하위의 경로)
     * @return Resource 자원
     */
    public static Resource getResource(String location) throws IOException {
        Resource[] resources = getResources(location);
        return resources.length > 0 ? resources[0] : null;
    }

}
