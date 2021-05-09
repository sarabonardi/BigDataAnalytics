import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CleanMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[] words = line.split("\\t", -1);
        if (words[0].equals("id") || words.length < 10) {
            return;
        }

        String output = words[3] + "\t" + words[10];
        Text outputKey = new Text(output);
        IntWritable outputValue = new IntWritable(1);
        context.write(outputKey, outputValue);
    }
}
