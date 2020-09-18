package com.syc.suda.util

import com.syc.suda.entity.AutoTestCaseConfiguration
import groovy.util.logging.Slf4j

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission
import java.text.SimpleDateFormat

@Slf4j
class ShellUtil {
    static String runShell(String shStr) throws Exception {
        String[] cmd= ["/bin/sh", "-c", shStr]
        Process process
        process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
        BufferedReader read = new BufferedReader(new InputStreamReader(process.getInputStream()))
        String line
        String result = ""
        while ((line = read.readLine())!=null){
            result+=line;
        }
        return result;
    }

    static void sshClientToExec(String dir,String content){
        FileOutputStream f = new FileOutputStream(dir);
        f.write(content.getBytes("utf-8"))
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.GROUP_READ)
        perms.add(PosixFilePermission.OTHERS_READ)
        perms.add(PosixFilePermission.OWNER_READ)
        perms.add(PosixFilePermission.GROUP_WRITE)
        perms.add(PosixFilePermission.OWNER_WRITE)
        perms.add(PosixFilePermission.OTHERS_WRITE)
        perms.add(PosixFilePermission.GROUP_EXECUTE)
        perms.add(PosixFilePermission.OTHERS_EXECUTE)
        perms.add(PosixFilePermission.OWNER_EXECUTE)
        Path path = Paths.get(dir)
        Files.setPosixFilePermissions(path, perms)
        String cmd = "sh " + dir
        log.info(cmd)
        File file = new File(".");
        String[] evnp = new String[2]
        evnp[0] = "val=2"
        evnp[1] = "call=Bash Shell"
        Process process = Runtime.getRuntime().exec(cmd, evnp, file)
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))
        String line = "";
        while ((line = input.readLine()) != null) {
            log.info(line);
        }
        input.close()
    }
}
