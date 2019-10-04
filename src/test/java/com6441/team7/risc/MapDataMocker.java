package com6441.team7.risc;

import com6441.team7.risc.api.model.Continent;
import com6441.team7.risc.api.model.Country;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MapDataMocker {
    @Test
    public void test1() throws Exception{
        String a = "editcontinent -add Asia 5 -add America 6";
//        String[] b = StringUtils.split(a, "-");
//        Arrays.stream(b).forEach(System.out::println);

        a = StringUtils.substringAfter(a, "-");
        System.out.println(a);
    }


}
