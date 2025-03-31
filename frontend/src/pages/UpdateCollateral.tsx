import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { api } from '../utils/api';
import { Collateral } from '../types/collateral';
import { useParams, useNavigate } from 'react-router-dom';
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

const UpdateCollateral: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
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

  useEffect(() => {
    const fetchCollateral = async () => {
      if (!id) {
        navigate('/');
        return;
      }

      try {
        setIsLoading(true);
        const collateral = await api.get<Collateral>(`/api/collateral/${id}`);
        form.reset({
          name: collateral.name,
          description: collateral.description,
          type: collateral.type
        });
      } catch (error) {
        console.error('Error fetching collateral:', error);
        setSubmitError('Failed to load collateral data. Please try again.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchCollateral();
  }, [id, form, navigate]);

  const onSubmit = async (data: CollateralFormData) => {
    if (!id) return;
    
    setIsSubmitting(true);
    setSubmitError(null);
    
    try {
      const result = await api.put<Collateral>(`/api/collateral/${id}`, data);
      console.log('Collateral updated:', result);
      setSubmitSuccess(true);
      setTimeout(() => {
        navigate(-1);
      }, 1500);
    } catch (error) {
      console.error('Error updating collateral:', error);
      setSubmitError('Failed to update collateral. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div className="container mx-auto p-6">
        <p className="text-center">Loading collateral data...</p>
      </div>
    );
  }

  return (
    <div className="container mx-auto p-6">
      <Card>
        <CardHeader>
          <CardTitle>Update Collateral</CardTitle>
          <CardDescription>Update the collateral entry by modifying the datapoints below.</CardDescription>
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
              
              <div className="flex justify-end space-x-2">
                <Button 
                  type="button" 
                  variant="outline" 
                  onClick={() => navigate(-1)}
                  disabled={isSubmitting}
                >
                  Cancel
                </Button>
                <Button type="submit" disabled={isSubmitting}>
                  {isSubmitting ? 'Updating...' : 'Update'}
                </Button>
              </div>
            </form>
          </Form>
          
          {submitSuccess && (
            <div className="mt-4 p-4 rounded bg-green-50 text-green-700">
              Collateral updated successfully!
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

export default UpdateCollateral;
