/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cp_c;

/**
 *
 * @author rajor
 */
import static cp_c.WC_Helper.getMostPopularWords;
import static cp_c.WC_Helper.getRank;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CP_C {

    public static final String[] keywords = {"education", "economy", "sports", "government"};

    public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {

        private final static IntWritable one = new IntWritable(1);
        private static final Pattern UNDESIRABLES = Pattern.compile("[(){},.;!+\"?<>%=*#]");

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            StringTokenizer itr = new StringTokenizer(UNDESIRABLES.matcher(value.toString()).replaceAll(" ").trim());
            int[] A = new int[keywords.length];
            for (int i = 0; i < A.length; i++) {
                A[i] = 0;
            }
            String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
            while (itr.hasMoreTokens()) {

                //remove unwanted characters 
                //to count words that end or start with special character
                String word = UNDESIRABLES.matcher(itr.nextToken().toString()).replaceAll("");
                word = word.toLowerCase();
                if (word.length() >= 5) {
                    context.write(new Text("total"), new Text(fileName + "-" + word));
                }
            }

        }
    }

    public static int index(String x) {
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].equals(x)) {
                return i;
            }
        }
        return -1;
    }

    public static int max_index(int[] a) {
        int ret = 0;
        int max = 0;
        // int[] si = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
                ret = i;
            }
        }
        return ret;
    }

    public static class IntSumReducer extends Reducer<Text, Text, Text, Text> {

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            HashMap<String, HashMap<String, Integer>> db = new HashMap<String, HashMap<String, Integer>>();
            for (Text val : values) {
                String value = val.toString();
                StringTokenizer strtok = new StringTokenizer(value, "-");
                String country = strtok.nextToken();
                String word = strtok.nextToken();
                WC_Helper.incWord(db, country, word);

            }

            for (String country : db.keySet()) {
                HashMap<String, Integer> c_db = db.get(country);
                String rank = getMostPopularWords(c_db);
                //  System.out.println(c_db);

                context.write(new Text(country), new Text(rank));

            }

        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(CP_C.class);

        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(IntSumReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
