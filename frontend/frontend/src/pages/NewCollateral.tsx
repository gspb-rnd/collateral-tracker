import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { api } from '../utils/api';
import { Collateral } from '../types/collateral';
import { 
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter
} from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Textarea } from "../components/ui/textarea";
import { Button } from "../components/ui/button";
import { 
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormDescription,
  FormMessage
} from "../components/ui/form";

interface CollateralFormData {
  name: string;
  description: string;
  type: string;
}

const NewCollateral: React.FC = () => {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitSuccess, setSubmitSuccess] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);
  
  const form = useForm<CollateralFormData>({
    defaultValues: {
      name: '',
      description: '',
      type: ''
    }
  });

  const onSubmit = async (data: CollateralFormData) => {
    setIsSubmitting(true);
    setSubmitError(null);
    
    try {
      const result = await api.post<Collateral>('/api/collateral', data);
      console.log('Collateral created:', result);
      setSubmitSuccess(true);
      form.reset();
    } catch (error) {
      console.error('Error creating collateral:', error);
      setSubmitError('Failed to create collateral. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container mx-auto p-6">
      <Card>
        <CardHeader>
          <CardTitle>New Collateral</CardTitle>
          <CardDescription>Create a new collateral entry by filling in the datapoints below.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Collateral Name</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter the name of the collateral" {...field} />
                    </FormControl>
                    <FormDescription>
                      This is the name that will be used to identify the collateral.
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              
              <FormField
                control={form.control}
                name="description"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Description</FormLabel>
                    <FormControl>
                      <Textarea 
                        placeholder="Enter a description of the collateral" 
                        className="min-h-[80px]" 
                        {...field} 
                      />
                    </FormControl>
                    <FormDescription>
                      Provide details about the collateral.
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              
              <FormField
                control={form.control}
                name="type"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Type</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter the type of collateral" {...field} />
                    </FormControl>
                    <FormDescription>
                      Specify the category or type of the collateral.
                    </FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              
              <div className="flex justify-end">
                <Button type="submit" disabled={isSubmitting}>
                  {isSubmitting ? 'Creating...' : 'Create'}
                </Button>
              </div>
            </form>
          </Form>
          
          {submitSuccess && (
            <div className="mt-4 p-4 rounded bg-green-50 text-green-700">
              Collateral created successfully!
            </div>
          )}
          
          {submitError && (
            <div className="mt-4 p-4 rounded bg-red-50 text-red-700">
              {submitError}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default NewCollateral;
