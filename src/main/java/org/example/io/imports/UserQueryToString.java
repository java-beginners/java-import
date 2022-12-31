package org.example.io.imports;

import java.util.List;

public class UserQueryToString {
    public String[] a = new String[]{"Id:", "Username:", "Email:", "Phone:", "Status:", "Create Date:"};

    public String chageString(List<String> user) {
        String st = user.toString();
        int count = 0;
        int n = 0;
        String id = "";
        st = st.replace("[", "");
        st = st.replace("]", "");
        st = st.replace(",", "");
        for (int i = 0; i < st.length(); i++) {
            if (st.charAt(i) == '\'') {
                count++;
                if (count % 2 != 0) {
                    if (n != 4) st = st.substring(0, i) + a[n] + st.substring(i + 1);
                    else {
                        if (st.charAt(i + 1) == '1') st = st.substring(0, i) + a[n] + "true" + st.substring(i + 2);
                        else st = st.substring(0, i) + a[n] + "false" + st.substring(i + 1);
                    }
                    n = n + 1;
                } else st = st.substring(0, i) + st.substring(i + 1);
            }
        }
        for (int i=3;i<=st.length();i++)
            if (st.charAt(i)!=' ')
                id=id+st.charAt(i); else break;
        st = st + "} . Error: Error 1062: Duplicate entry '" + id + "' for key 'usersimport.PRIMARY'\"}";
        return st;
    }
}