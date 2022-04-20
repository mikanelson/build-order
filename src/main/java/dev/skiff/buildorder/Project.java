package dev.skiff.buildorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private HashMap<String, ArrayList<String>> projectInfo;
}
