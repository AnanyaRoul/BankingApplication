package com.example.demo;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FileFunctionTest {

    @InjectMocks
    private FileFunction mockFileFunc;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
      
        Field field = FileFunction.class.getDeclaredField("Upload_DIR");
        field.setAccessible(true);
        field.set(mockFileFunc, "test_upload_dir/");
        
    }
    
    @Test
    public void testUploadFile() {
       
        String fileName = "testFile.txt";
        String filePath = mockFileFunc.UploadFile(fileName);

        assertEquals("test_upload_dir/testFile.txt", filePath);
    }


}
